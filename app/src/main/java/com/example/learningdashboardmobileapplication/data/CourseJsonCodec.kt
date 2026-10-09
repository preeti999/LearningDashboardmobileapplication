package com.example.learningdashboardmobileapplication.data

import org.json.JSONArray
import org.json.JSONObject

object CourseJsonCodec {
    fun parseCourses(json: String): List<Course> {
        val coursesArray = JSONArray(json)
        return buildList {
            for (index in 0 until coursesArray.length()) {
                val courseObject = coursesArray.getJSONObject(index)
                add(
                    Course(
                        id = courseObject.getInt("id"),
                        title = courseObject.getString("title"),
                        instructor = courseObject.getString("instructor"),
                        progress = courseObject.getInt("progress"),
                        lessons = courseObject.getInt("lessons")
                    )
                )
            }
        }
    }

    fun toJson(courses: List<Course>): String {
        val coursesArray = JSONArray()
        courses.forEach { courseArrayItem ->
            coursesArray.put(
                JSONObject().apply {
                    put("id", courseArrayItem.id)
                    put("title", courseArrayItem.title)
                    put("instructor", courseArrayItem.instructor)
                    put("progress", courseArrayItem.progress)
                    put("lessons", courseArrayItem.lessons)
                }
            )
        }
        return coursesArray.toString(2)
    }
}

