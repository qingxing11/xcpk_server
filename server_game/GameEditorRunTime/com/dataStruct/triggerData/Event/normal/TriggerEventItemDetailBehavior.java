package com.dataStruct.triggerData.Event.normal;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.dataStruct.triggerData.Event.TriggerEventDataParent;

@Service
@Scope("prototype")
public class TriggerEventItemDetailBehavior extends TriggerEventDataParent {
	/**
	 * EnumItemType中对应的值
	 */
	public float item_type;
	public float item_id;

	/**
	 * @param obj:obj[0]itme_type,obj[1]item_id
	 */
	@Override
	public boolean DataProcessing(float origId, Object... obj) {
		if ((int) origId != (int) this.origId) {
			return false;
		}

		if (obj.length < 2) {
			return false;
		}

		if ((int) obj[0] != (int) item_type) {
			return false;
		}

		if ((int) obj[1] == (int) item_id) {
			return true;
		}

		return false;
	}

}
