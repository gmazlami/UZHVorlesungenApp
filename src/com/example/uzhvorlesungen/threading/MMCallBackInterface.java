package com.example.uzhvorlesungen.threading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MMCallBackInterface {

	void onTaskCompleted(HashMap<String, String> map,ArrayList<String> faculties, Map<String, List<String>> facultiesMap, Map<String, String> titlesMap);
}
