package com.example.uzhvorlesungen.threading;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface FacultiesCallback{

	void onTaskCompleted(ArrayList<String> faculties, Map<String, List<String>> map);
}
