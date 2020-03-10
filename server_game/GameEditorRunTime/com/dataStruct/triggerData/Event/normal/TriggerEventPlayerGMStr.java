package com.dataStruct.triggerData.Event.normal;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.dataStruct.triggerData.Event.TriggerEventDataParent;
@Scope("prototype")
@Service
public class TriggerEventPlayerGMStr extends TriggerEventDataParent {
	public String GM_str;

	@Override
	public boolean DataProcessing(float origId, Object... obj) {

		if ((int) this.origId != (int) origId) {
			return false;
		}

		if (obj.length <= 0) {
			return false;
		}

		String str = obj[0].toString();

		// String[] strs = str.split(" ");
		// if (strs.length <1) {
		// return false;
		// }

		if (str.startsWith(GM_str)) {
			return true;
		}

		return false;
	}

}
