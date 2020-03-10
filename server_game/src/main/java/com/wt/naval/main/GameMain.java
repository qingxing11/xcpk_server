package com.wt.naval.main;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import com.wt.cmd.MsgType;
import com.wt.cmd.Response;
import com.wt.cmd.serverpush.PushUser_ServerReboot;
import com.wt.event.server.ServerEvent;
import com.wt.event.server.shutdown.ShutDownEvent;
import com.wt.factory.MyBeanFactory;
import com.wt.iserver.IServer;
import com.wt.iserver.MyNetty;
import com.wt.naval.biz.ServerBiz;
import com.wt.naval.cache.ServerCache;
import com.wt.naval.cache.UserCache;
import com.wt.naval.server.GameServerImpl;
import com.wt.naval.vo.user.Player;
import com.wt.netty.client.ClientHelper;
import com.wt.server.config.MyConfig;
import com.wt.tool.PrintMsgTool;
import com.wt.util.MyTimeUtil;
import com.wt.util.Tool;
import com.wt.util.security.MySession;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

@SpringBootApplication //
@ComponentScan(basePackages = {"com","data.data"})
@EnableEurekaClient 
@EnableCaching
public class GameMain implements IServer
{
	@Value("${eureka.instance.nonSecurePort}")
	private int gamePort;

	@Value("${authIP}")
	private String authIP;
	@Value("${authPort}")
	private int authPort;

	private static long serverStartTime;

	public static GameMain inst;
	
// 	@Autowired
//	private WorldMapService mapService;
	public static void main(String[] args)
	{
		serverStartTime = MyTimeUtil.getCurrTimeMM();
		Tool.print_debug_level0("正在准备spring.....");
		ApplicationContext appContext = SpringApplication.run(GameMain.class, args);// 启动spring，向服务中心注册
		Tool.print_debug_level0("spring完成.....,appContext:"+appContext);
		MyBeanFactory.setApplicationContext(appContext);
		initConfig();
		ServerCache.init();
		initFilterPrintMsg();
		ServerEvent.gameServerStartup();
	}
	
	public GameMain() {	}
	
	@PostConstruct
	private void init()
	{
		inst = this;
		initAuthHost();
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				MyNetty.instance().start(serverStartTime, gamePort, inst);
			}
		}).start();
	}

	private void initAuthHost()
	{
		System.out.println("initAuthHost ===== authIP:" + authIP + ",authPort:" + authPort);
		ClientHelper.initAuth(authIP, authPort);
	}

	private static void initFilterPrintMsg()
	{
		PrintMsgTool.init();
	}

	private static void initConfig()
	{
		Tool.setPrintLevel(MyConfig.instance().print_debug_leven);
		Tool.setPrintLine(MyConfig.instance().print_line);
	}

	@Override
	public void shutDown()
	{
//		noticeAllUser();

		ServerBiz.saveServerData(GameServerImpl.instance);
		Tool.print_debug_level0("保存服务器数据完成.");

		ShutDownEvent.shutdown();
		
//		saveAndOfflineAllUser();

		Tool.print_debug_level0("完成关闭服务,优雅退出完成");
	}

	private void noticeAllUser()
	{
		Tool.print_debug_level0("当前需要广播人数:" + UserCache.getAllChannel().values().size());
		PushUser_ServerReboot serverReboot = new PushUser_ServerReboot();
		for (Channel iterable_element : UserCache.getAllChannel().values())
		{
			if(iterable_element.isActive())
			{
				GameServerHelper.sendResponse(iterable_element, serverReboot);
			}
		
		}
	}

	private void saveAndOfflineAllUser()
	{
		long startTime = MyTimeUtil.getCurrTimeMM();
		Tool.print_debug_level0("开始保存并离线所有玩家。。。");

		for (Integer userId : UserCache.getAllChannel().keySet())
		{
			UserCache.offline(userId);
		}

		Tool.print_debug_level0("保存并离线所有玩家完成，耗时:" + (MyTimeUtil.getCurrTimeMM() - startTime));
	}

	/**
	 * 玩家断开连接
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx)
	{
		// LogUtil.print_debug_level0("channelInactive。。。。。。。。。。。。。");
		MySession session = ctx.channel().attr(MySession.attr_session).get();
		if (session != null)
		{
			Tool.print_debug_level0("客户端断开链接,session:"+session.getUserId());
			UserCache.autoOffline(ctx.channel());
		}
		else
		{
			Tool.print_debug_level0("未登录通道断开");
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object msg)
	{
		boolean isDebug = false;

		if (msg instanceof IdleStateEvent)
		{
			Channel channel = ctx.channel();
			if (channel == null)
			{
				return;
			}
			String nickName = "";

			MySession session = channel.attr(MySession.attr_session).get();
			if (session == null)
			{
				return;
			}

			if (isDebug)
				Tool.print_debug_level0("通道超时:" + session.getUserId());

			int userId = session.getUserId();
			Player player = UserCache.getPlay(userId);
			if (player != null)
			{
				nickName = player.getNickName();
			}
			else
			{
				if (isDebug)
					System.out.println("玩家null");
			}

			if (session.isOffline())
			{
				return;
			}

			IdleStateEvent e = (IdleStateEvent) msg;
			if (e.state() == IdleState.READER_IDLE)// 指定时间内接受到数据
			{
				session.addIdleNum();
				Response response = new Response();
				response.msgType = MsgType.HEARTBEAT;
				GameServerHelper.sendResponse(ctx, response);

				if (isDebug)
					System.out.println("READER_IDLE 读超时,idleNum:" + session.getIdleNum() + ",nickName:" + nickName);
				if (session.getIdleNum() > 2)
				{
					Tool.print_error("READER_IDLE,nickName:" + nickName);
					UserCache.autoOffline(ctx.channel());
				}
			}
			else if (e.state() == IdleState.WRITER_IDLE)// 指定时间内没有写数据
			{
				Response response = new Response();
				response.msgType = MsgType.HEARTBEAT;
				GameServerHelper.sendResponse(ctx, response);

				if (isDebug)
					System.out.println("WRITER_IDLE,nickName:" + nickName);
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	{
		Tool.print_debug_level0("exceptionCaught。。。。。。。。。。。。。");
		Channel incoming = ctx.channel();
		if (!incoming.isActive())
			System.out.println("SimpleClient:" + incoming.remoteAddress());

		cause.printStackTrace();
	}
}
