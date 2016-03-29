package com.bonboa.fabric;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.LoginEvent;
import com.crashlytics.android.answers.SignUpEvent;

import android.util.Log;

import java.lang.reflect.Method;
import java.util.Iterator;

public class FabricPlugin extends CordovaPlugin {
	private final String pluginName = "FabricPlugin";

	@Override
	public boolean execute(final String action, final JSONArray data, final CallbackContext callbackContext) {
		Log.d(pluginName, pluginName + " called with options: " + data);

		if (action.equals("sendCrash")) {
			sendCrash(data, callbackContext);
		} else if (action.equals("addLog")) {
			addLog(data, callbackContext);
		} else if (action.equals("setUserIdentifier")) {
			setUserIdentifier(data, callbackContext);
		} else if (action.equals("setUserName")) {
			setUserName(data, callbackContext);
		} else if (action.equals("setUserEmail")) {
			setUserEmail(data, callbackContext);
		} else if (action.equals("setStringValueForKey")) {
			setStringValueForKey(data, callbackContext);
		} else if (action.equals("setIntValueForKey")) {
			setIntValueForKey(data, callbackContext);
		} else if (action.equals("setBoolValueForKey")) {
			setBoolValueForKey(data, callbackContext);
		} else if (action.equals("setFloatValueForKey")) {
			setFloatValueForKey(data, callbackContext);
		} else {
			Class params[] = new Class[2];
			params[0] = JSONArray.class;
			params[1] = CallbackContext.class;

			try{
				Method method = this.getClass().getDeclaredMethod(action, params);
				method.invoke(this, data, callbackContext);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return true;
	}

	/* Answers Events */

	public void sendSignUp(final JSONArray data, final CallbackContext context) {
		this.cordova.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				SignUpEvent evt = new SignUpEvent();
				Answers.getInstance()
					.logSignUp(evt);
			}
		});
	}

	public void sendLogIn(final JSONArray data, final CallbackContext context) {
		this.cordova.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				LoginEvent evt = new LoginEvent();
				Answers.getInstance()
					.logLogin(evt);
			}
		});
	}

	public void sendContentView(final JSONArray data, final CallbackContext context) {
		final JSONObject obj = data.optJSONObject(0);

		this.cordova.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ContentViewEvent evt = new ContentViewEvent();

				evt.putContentName(obj.optString("name"));
    			evt.putContentType(obj.optString("type"));
    			evt.putContentId(obj.optString("id"));

				if( obj.has("attributes") ) {
					JSONObject json = obj.optJSONObject("attributes");

					Iterator<String> keys = json.keys();
				    while(keys.hasNext()){
				        String key = keys.next();

						try {
							evt.putCustomAttribute(key, json.getString(key));
						} catch(Exception e) {}
				    }
				}

				Answers.getInstance().logContentView(evt);
			}
		});
	}

	public void sendCustomEvent(final JSONArray data, final CallbackContext context) {
		final JSONObject obj = data.optJSONObject(0);

		this.cordova.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				CustomEvent evt = new CustomEvent(obj.optString("name"));

				if( obj.has("attributes") ) {
					JSONObject json = obj.optJSONObject("attributes");

					Iterator<String> keys = json.keys();
				    while(keys.hasNext()){
				        String key = keys.next();

						try {
							evt.putCustomAttribute(key, json.getString(key));
						} catch(Exception e) {}
				    }

					Answers.getInstance().logCustom(evt);
				}
			}
		});
	}

	/* Answers Events */

	private void sendCrash(final JSONArray data,
			final CallbackContext callbackContext) {
		this.cordova.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				throw new RuntimeException("This is a crash");
			}
		});
	}

	private void sendNonFatalCrash(final JSONArray data,
			final CallbackContext callbackContext) {
		this.cordova.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Crashlytics.logException(new Throwable(
						"Sending non fatal crash from JS"));
			}
		});
	}

	private void addLog(final JSONArray data,
			final CallbackContext callbackContext) {
		try {
			JSONObject obj = data.getJSONObject(0);
			String message;
			if (obj.has("message")) {
				message = obj.getString("message");
				Crashlytics.log(message);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void setUserIdentifier(final JSONArray data,
			final CallbackContext callbackContext) {
		String identifier = getValueFromData(data);
		if (identifier != null) {
			Crashlytics.setUserIdentifier(identifier);
		}
	}

	private void setUserName(final JSONArray data,
			final CallbackContext callbackContext) {
		String userName = getValueFromData(data);
		if (userName != null) {
			Crashlytics.setUserName(userName);
		}
	}

	private void setUserEmail(final JSONArray data,
			final CallbackContext callbackContext) {
		String email = getValueFromData(data);
		if (email != null) {
			Crashlytics.setUserEmail(email);
		}
	}

	private void setStringValueForKey(final JSONArray data,
			final CallbackContext callbackContext) {
		String value = getValueFromData(data);
		String key = getKeyFromData(data);
		if (value != null && key != null) {
			Crashlytics.setString(key, value);
		}
	}

	private void setIntValueForKey(final JSONArray data,
			final CallbackContext callbackContext) {
		int value = -1;
		try {
			JSONObject obj = data.getJSONObject(0);
			if (obj.has("value")) {
				value = obj.getInt("value");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String key = getKeyFromData(data);
		if (value != -1 && key != null) {
			Crashlytics.setInt(key, value);
		}
	}

	private void setBoolValueForKey(final JSONArray data,
			final CallbackContext callbackContext) {
		boolean value = true;
		try {
			JSONObject obj = data.getJSONObject(0);
			if (obj.has("value")) {
				value = obj.getBoolean("value");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String key = getKeyFromData(data);
		if (key != null) {
			Crashlytics.setBool(key, value);
		}
	}

	private void setFloatValueForKey(final JSONArray data,
			final CallbackContext callbackContext) {
		float value = -1;
		try {
			JSONObject obj = data.getJSONObject(0);
			if (obj.has("value")) {
				value = obj.getLong("value");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String key = getKeyFromData(data);
		if (value != -1 && key != null) {
			Crashlytics.setFloat(key, value);
		}
	}

	private String getValueFromData(JSONArray data) {
		try {
			JSONObject obj = data.getJSONObject(0);
			if (obj.has("value")) {
				return obj.getString("value");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getKeyFromData(JSONArray data) {
		try {
			JSONObject obj = data.getJSONObject(0);
			if (obj.has("key")) {
				return obj.getString("key");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
