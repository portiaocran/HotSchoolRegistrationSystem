//Portia Ocran
//991545021

//This class creates an observable array list that will hold courses

package HotSchool;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CourseList {
	
	private ObservableList<Course> courseList;
	
	
	CourseList(){
		
		this.courseList = FXCollections.observableArrayList();
		
	}
	
	public void add(Course course) {
		this.courseList.add(course);
	}
	
	public void remove (Course course) {
		this.courseList.remove(course);
	}

	public ObservableList<Course> getCourseList() {
		return courseList;
	}
	
	 @Override
	   public String toString ()
	   {
	      String course = "";
	      for (Course a : this.courseList) {
	    	 course += a.getId() + " : ";
	         course += a.getCourseName() + "\t";
	         course += a.getStartTime();

	      }
	      return course;
	   }


}
