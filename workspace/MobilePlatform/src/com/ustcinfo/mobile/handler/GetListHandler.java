package com.ustcinfo.mobile.handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ustcinfo.mobile.platform.domain.HttpGetListResult;


/**
 * @since SasMobile 1.0.0
 * @version 1.0 2011-7-11
 * @author ycxiong
 */
public class GetListHandler extends JsonHandler<HttpGetListResult> {
	private String arrayKey; // json字符串中需要提取数组的key名称

	public GetListHandler(String arrayKey) {
		super();
		this.arrayKey = arrayKey;
	}

	/**
	 * @param jsonObj
	 * @throws JSONException
	 * @return
	 * @see cn.com.starit.sas.mobile.handlers.JsonHandler#extractFromJson(org.json.JSONObject)
	 */
	@Override
	protected HttpGetListResult extractFromJson(JSONObject jsonObj)
			throws JSONException {
		HttpGetListResult httpGetListResult = new HttpGetListResult();
		String result = jsonObj.getString("result");
		String desc = jsonObj.getString("desc");
		JSONArray jsonArray = jsonObj.getJSONArray(arrayKey);
		httpGetListResult.setResult(result);
		httpGetListResult.setDesc(desc);
		httpGetListResult.setListMap(jsonArrayToListMap(jsonArray));

		return httpGetListResult;
	}

}
