package com.example.uzhvorlesungen.threading;

import com.example.uzhvorlesungen.parsers.Lecture;

public interface DetailsCallbackInterface {

	void onTaskCompleted(Lecture lecture);
}
