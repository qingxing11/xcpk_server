package com.dataStruct.triggerData.Event.normal;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.dataStruct.triggerData.Event.TriggerEventDataParent;
import com.wt.util.Tool;

@Service
@Scope("prototype")
public class TriggerEventPCComType extends TriggerEventDataParent {
	public String GM_str;

	/// <summary>
	/// 具体的id
	/// </summary>
	public float item_id;

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
		if (strs.length < 2) {
			return false;
		}

		int id = -1;

		try {
			id = Integer.parseInt(strs[1]);
		} catch (Exception e) {
			Tool.print_debug_level0("输入了错误的GM指令");
			return false;
		}

		if (id != (int) item_id) {
			return false;
		}

		return true;
	}

}
