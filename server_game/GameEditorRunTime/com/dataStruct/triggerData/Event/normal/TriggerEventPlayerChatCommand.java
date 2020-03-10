package com.dataStruct.triggerData.Event.normal;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.dataStruct.triggerData.Event.TriggerEventDataParent;
import com.wt.util.Tool;

@Scope("prototype")
@Service
public class TriggerEventPlayerChatCommand extends TriggerEventDataParent {

	public String GM_str;
	public float item_id;
	public float item_num;

	/**
	 * @param obj:聊天内容
	 */
	@Override
	public boolean DataProcessing(float origId, Object... obj) {

		if ((int) this.origId != (int) origId) {
			return false;
		}

		if (obj.length <= 0) {
			return false;
		}

		String str = obj[0].toString();
		if (!str.startsWith(GM_str)) {
			return false;
		}

		String[] strs = str.split(" ");
		if (strs.length < 3) {
			return false;
		}

		int id = -1, num = -1;

		try {
			id = Integer.parseInt(strs[1]);
			num = Integer.parseInt(strs[2]);
		} catch (Exception e) {
			// Tool.print_error(e);
			Tool.print_debug_level0("输入了错误的GM指令");

			return false;
		}

		if (id != (int) item_id) {
			return false;
		}

		if (num == (int) item_num) {
			return true;
		}

		return false;
	}

}
