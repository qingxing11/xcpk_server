package com.dataStruct.triggerData.Event.normal;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.dataStruct.triggerData.Event.TriggerEventDataParent;
import com.wt.util.Tool;

@Scope("prototype")
@Service
public class TriggerEventItemTypeBehavior extends TriggerEventDataParent {
	/**
	 * EnumItemType中对应的值
	 */
	public float item_type;

	@Override
	public boolean DataProcessing(float origId, Object... obj) {
		Tool.print_debug_level0("TriggerEventItemTypeBehavior：origId=" + origId + ",this.origId=" + this.origId);

		if ((int) origId != (int) this.origId) {
			return false;
		}

		if (obj != null && obj.length > 0) {
			if ((int) obj[0] == (int) item_type) {
				Tool.print_debug_level0("TriggerEventItemTypeBehavior中判断结果为真。");
				return true;
			}
		}

		Tool.print_debug_level0("TriggerEventItemTypeBehavior中判断结果为假。");
		return false;
	}

}
