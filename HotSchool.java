/*Portia Ocran
  991545021*/

package HotSchool;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HotSchool extends Application {
	
	Connection conn = null;
	//All variables below are used for GUI(Javafx)
	private final BorderPane paneMain = new BorderPane();
	private final BorderPane paneTitle = new BorderPane();
	private final VBox buttons = new VBox(10);
	private final Label titleMan = new Label("HS Enrollment Manager");
	//Radio buttons to select an option
	private final Button viewCourse = new Button("View Students Enrolled in Course");
    private final Button viewStudents = new Button("View Courses a Student is Enrolled in");
    private final Button addCourse = new Button("Add Course");
    private final Button addStudent = new Button("Add Student");
    private final Button remCourse = new Button("Remove Course");
    private final Button enrStudent = new Button("Enroll or Un-enroll a Student");
    private final Button cancel = new Button("Exit HS Manager");
    Course c1 = new Course();
    Student s1= new Student();
    
    //CourseList object created which contains an array list that will hold all courses
    private static CourseList courseList = new CourseList();
    private static StudentList studentList = new StudentList();
    
    
	/*
	 * This courseList is only for the unenroll and enroll screens. It will populate
	 * with courses that the user is in, or if enrolling a student in a course it
	 * will populate with courses that the user isn't registered in
	 */    private static CourseList courseListEnr = new CourseList();
  
    
	@Override
    public void start (Stage primaryStage) {
		
		//Required elements to sign into database
		String connURL= "jdbc:mysql://localhost:3306/hotsummer";
		String user= "root";
		String pass= "Aitrop31";
		
		//Calling methods to connect to database, read items from database into respective arrays
		connectToDB(connURL, user, pass);
		readFromDb();
		readCoursesFromDB();
		addCourseToSL();
		
		//All information below is strictly for appearance purposes (JAVA FX), not functionality
		paneTitle.setPadding(new Insets(30));
		buttons.setPadding(new Insets(20,20,20,20));
		paneMain.setPadding(new Insets(30));
		viewCourse.prefWidthProperty().bind(buttons.widthProperty().subtract(10));
		viewStudents.prefWidthProperty().bind(buttons.widthProperty().subtract(10));
		addCourse.prefWidthProperty().bind(buttons.widthProperty().subtract(10));
		addStudent.prefWidthProperty().bind(buttons.widthProperty().subtract(10));
		remCourse.prefWidthProperty().bind(buttons.widthProperty().subtract(10));
		enrStudent.prefWidthProperty().bind(buttons.widthProperty().subtract(10));
		cancel.prefWidthProperty().bind(buttons.widthProperty().subtract(10));
  
		titleMan.setAlignment(Pos.CENTER);
		titleMan.setFont(new Font("Sans Serif", 30));
		paneTitle.setAlignment(titleMan,Pos.CENTER);           
		           
		buttons.getChildren().add(viewCourse);
		buttons.getChildren().add(viewStudents);
		buttons.getChildren().add(addCourse);
    
		buttons.getChildren().add(addStudent);
		buttons.getChildren().add(remCourse);
		buttons.getChildren().add(enrStudent);
		buttons.getChildren().add(cancel);
      
		paneMain.setTop(titleMan);
		paneMain.setCenter(buttons);
      
		Scene mainScene = new Scene(paneMain, 400, 350);
		
		primaryStage.setTitle("Hot School Enrollment Manager");
		primaryStage.setScene(mainScene);
		primaryStage.show();
		
		// Event handlers for all buttons
		addCourse.setOnAction(e -> addCourse());
		addStudent.setOnAction(a -> addStudent());
		enrStudent.setOnAction(b -> enrStudent());
		remCourse.setOnAction(c -> remCourse());
		viewStudents.setOnAction(d ->checkStudentCourses());
		viewCourse.setOnAction(f -> checkCourses());
		cancel.setOnAction(e -> {
			Platform.exit();
			
		//When a user exits the system, the connection will close
					try {
						conn.close();
						System.out.println("Connection ended");
					} catch (SQLException w) {
						// TODO Auto-generated catch block
						w.printStackTrace();
					}
				});
						
	}
	
//	Add course method will add a course to the course list which will in turn update the database
	private void addCourse() {
		
		//All variables below were created for appearance, not functionality
		final Label addCourseLabel = new Label("Add Course");
		final Label addStudentLabel = new Label("Add Student");
		 
		GridPane addCourseGP = new GridPane();
		TextField courseNameTF = new TextField();
	    TextField courseTimeTF = new TextField("hh:mm:ss");
	    Stage addCourseStage = new Stage();
	    BorderPane addCoursePane = new BorderPane();
	    BorderPane addCourseTitlePane = new BorderPane();
	    
		BorderPane aCBP = new BorderPane();
		HBox HB = new HBox(25);
		HB.setAlignment(Pos.CENTER);
		HB.setPadding(new Insets(10,10,40,10));
		Button addOK2 = new Button("ADD");
		Button addCancel2 = new Button("Cancel");
		addCourseLabel.setFont(new Font("Sans Serif", 15));
		addCourseGP.setPadding(new Insets(15,15,15,15));
		aCBP.setCenter(addCourseLabel);
		aCBP.setAlignment(addCourseLabel,Pos.CENTER);
		aCBP.setPadding(new Insets(10));
		addCoursePane.setPadding(new Insets(20,20,20,20));
		addCourseGP.setHgap(15);
		addCourseGP.setVgap(20);
		addCourseGP.add(new Label("Course Name:"), 0, 0);
		addCourseGP.add(courseNameTF,0 , 1);
		addCourseGP.add(new Label("Course Time:    "), 1, 0);
		addCourseGP.add(courseTimeTF,1 , 1);
		addCoursePane.setTop(aCBP);
		addCoursePane.setCenter(addCourseGP);
		HB.getChildren().add(addOK2);
		HB.getChildren().add(addCancel2);
		
		addCoursePane.setBottom(HB);
		
		addCourseStage.setTitle("Add Course");
		final Scene addCourseScene= new Scene(addCoursePane, 250, 200);
		addCourseStage.setScene(addCourseScene);
		addCourseStage.show();	
		
		//Event handler for using selecting add button
		addOK2.setOnAction( e ->
		{
			//Retrieving values from the course name and course time textboxes
			String courseName = courseNameTF.getText();
			String courseTime= courseTimeTF.getText();
			//Created a query to add the courses into the course list in the HotSchool database
			
			String insertSQL = " INSERT  INTO course (courseName, startTime) " + "VALUES  (?, ?)";
			PreparedStatement stmt = null;
			
			// If either values are empty, the user is alerted and must try again
			if(courseName.isEmpty() || courseTime.isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);                            
	            alert.setTitle("Please enter all values");
	            alert.setContentText("Please enter course name and time");
	            alert.showAndWait();		   
				}
			else {
					try {
						//if al values are inputted correctly, then the query can be prepared ad executed
						stmt = conn.prepareStatement(insertSQL);
						stmt.setString(1, courseName);
						stmt.setString(2, courseTime);
						System.out.println("query prepared");
						this.courseList.getCourseList().clear();
					/*
					 * We are calling the readCoursesFromDB() method to populate updated course list
					 * from database into our courses array
					 */
						readCoursesFromDB();		
						} catch (SQLException f) {
						f.printStackTrace();
						System.out.println("Unsucessful");
						}
					
					int numRowsAff = 0;
					
					try {
						//If execution of query is sucessful, we will display an alert
						try {
						numRowsAff = stmt.executeUpdate();
						 Alert alert = new Alert(AlertType.CONFIRMATION);                            
		                  alert.setTitle("Course Added");
		                  alert.setContentText(courseName + " has been added to the list of courses");
		                  alert.showAndWait();
		                  addCourseStage.close();
						System.out.println("Number of rows affected" + numRowsAff);
						/*
						 * the "DataTruncation" exception is for the time format when adding a course. If the time
						 * format is not the aligned with our time format in our database, there will be an
						 * error and the user will be prompted to re-enter the time
						 */
						}catch (DataTruncation truncationEx) {
		                Alert alert = new Alert(AlertType.ERROR);                            
		                  alert.setTitle("Incorrect Time Format");
		                  alert.setHeaderText("Incorrect Time Format");
		                  alert.setContentText("The time chosen is incorrect. Please choose a different time, "
		                  					+ "and follow this format : hh:ss:mm");
		                  alert.showAndWait();
						}
					}catch(SQLException a) {
						a.printStackTrace();
						System.out.println("Error excuting query");
					}
					//close our query
					try {
						stmt.close();
					} catch (SQLException a) {
						a.printStackTrace();
					}
		
		}
		
			// When the user selects cancel, the Add Course window will exit
		addCancel2.setOnAction(b -> {
			addCourseStage.close();	
			
		});
		});
		 
	}
	
	private void remCourse() {
		
		readFromDb();	
		 readCoursesFromDB();
		 
		//All variables are used for GUI(Javafx)
		final ListView<Course> courseLV = new ListView<>(FXCollections.observableArrayList());
		final Label remCourseLabel = new Label("Remove a course");
		final Label remChCourseLabel = new Label("Choose a course to remove");
		final BorderPane remCourseBorderPane = new BorderPane();
		final Button remCourseBt = new Button ("Remove Course");
		final Button remCancelBt = new Button ("Cancel");
		final ScrollPane courseLV2= new ScrollPane(courseLV);
		final VBox remCourseVB = new VBox(20);
		final BorderPane remcourseBP = new BorderPane();
		final Pane courseLvPane = new Pane();
		final Stage enrStudStage = new Stage();
		final Scene enrScene = new Scene(remCourseBorderPane,400,350);
		
			//Formatting for window appearance
			remCourseLabel.setFont(new Font("Sans Serif", 25));
			remChCourseLabel.setFont(new Font("Sans Serif", 15));
			courseLV.prefHeightProperty().bind(remCourseBorderPane.heightProperty().divide(2));
			remcourseBP.setPadding(new Insets(5,0,25,0));
			remCourseVB.setPadding(new Insets(50,0,0,20));
			remCourseVB.getChildren().add(remCourseBt);
			remCourseVB.getChildren().add(remCancelBt);
			courseLvPane.getChildren().add(courseLV2);
			remcourseBP.setTop(remCourseLabel);
			remcourseBP.setCenter(remChCourseLabel);
			remcourseBP.setAlignment(remChCourseLabel, Pos.CENTER);
			remcourseBP.setAlignment(remCourseLabel, Pos.CENTER);
			remCourseBorderPane.setTop(remcourseBP);
			remCourseBorderPane.setCenter(courseLvPane);
			remCourseBorderPane.setRight(remCourseVB);
			remCourseBorderPane.setPadding(new Insets(20,20,20,20));
			remCourseBorderPane.setAlignment(courseLvPane, Pos.CENTER);
			enrStudStage.setScene(enrScene);
			enrStudStage.show();
		
		remCancelBt.setOnAction(b -> {
			enrStudStage.close();
		});
		
		//Here, we are setting the items in the listview to the courses in the course list
		courseLV.setItems(courseList.getCourseList()); //adding students from list to listviewS
        courseLV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        courseLV.getSelectionModel().selectedItemProperty().addListener( //controls main screen. if user selects song, information appears on screen
                ov -> {
                           for (Integer i : courseLV.getSelectionModel().getSelectedIndices()) {
                        	   
                        	//Using the listener from the listview to select the course, and retrieve its name
                        	   
                        	String courseName = this.courseList.getCourseList().get(i).getCourseName();
                   			
                   			//Created a execute query to see if any students are currently enrolled in the course

                   			String selectSQL = "SELECT firstName, lastName FROM student " +
                   					"INNER JOIN studentcourse ON studentcourse.studentID = student.id " +
                   					"INNER JOIN course ON studentcourse.courseID = course.id " + 
                   					"WHERE courseName = ? ";

                        	remCourseBt.setOnAction(e-> {
                        		   
                        		   PreparedStatement stmt2 = null;
                            	   
                        			try {
                        				stmt2 = conn.prepareStatement(selectSQL);
                        				stmt2.setString(1, courseName);
                        			} catch (SQLException q) {
                        				q.printStackTrace();
                        				System.out.println("Unsuccesful");
                        			}
                        			
                        			ResultSet rs = null;
                        			
                        			try {
                        				rs = stmt2.executeQuery();
                        			} catch (SQLException z){
                        				z.printStackTrace();
                        				System.out.println("Unsucessful Query");	
                        			}
          
                        			try {
                        				//Created an if statement to check if there are any results from the following query 
	                        			if(rs.next()) {	
	                        			 try {
	                        				 
	                        					 String enrStudents="The following students are enrolled: \n\n";
	                        			//Storing the results (if any), into a string to be displayed
		                                			do {
		                                				String fName = rs.getString("firstName");
		                                				String lName = rs.getString("lastName");
		                                				
		                                				enrStudents+= fName + " " + lName + "\n";
		                                			} while(rs.next());
										/* if statement comes back with results, this means that students are enrolled
										 * in a course. The user will be alerted, and the course will not be removed.
										 * The user will be taken back to the screen, where they can try again*/
		                                			Alert alert = new Alert(AlertType.ERROR);                            
			                                        alert.setTitle("Course Cannot be removed");
			                                        alert.setHeaderText(courseName + " cannot not removed");
			                                        alert.setContentText(enrStudents);
			                                        alert.showAndWait();
		                                		} catch (SQLException e1) {
		                                			e1.printStackTrace();
		                                			System.out.println("Unsuccessul Query");
		                                		}
	                    				} 
	                        			else {
	                    					
	                        			// if there are no students enrolled in a course, then it will be removed using the query below
	                        				String deleteSQL = "DELETE FROM course WHERE courseName = ?";
                        	        
		                        			PreparedStatement stmt = null;
		                        			
		                        			try {		
		                        				stmt = conn.prepareStatement(deleteSQL);
		                        				stmt.setString(1, courseName);
		                        				System.out.println("query prepared");
		                        			} catch (SQLException d) {
		                        				d.printStackTrace();
		                        				System.out.println("Unsucessful");
		                        			}
		                        			
		                        			int numRowsAff = 0;
		                        			try {
		                        				numRowsAff = stmt.executeUpdate();
		                        				Alert alert = new Alert(AlertType.CONFIRMATION);                            
		                                        alert.setTitle("Course Successfully Removed");
		                                        alert.setContentText(courseName + " has been removed from the course list");
		                                        alert.showAndWait();
		                                        enrStudStage.close();
		                        				courseLV.refresh();
		                        				System.out.println("Number of rows affected" + numRowsAff);
		                        			} catch(SQLException w) {
		                        				w.printStackTrace();
		                        				System.out.println("Error excuting query");
		                        			}
		                    				
	                    				} 
                        			} catch (SQLException a) {
			                				a.printStackTrace();
			                				System.out.println("Unsuccessul Query");
			                        		}
                        			//Query closed after successful execution
                        		try {
                        				rs.close();
                        				stmt2.close();
                        			} catch (SQLException t) {
                        				// TODO Auto-generated catch block
                        				t.printStackTrace();
                        			}			
                           });
                       }
                            
                });
        
        //Updating courseList with new courses added
        readCoursesFromDB();             
	}


	private void addStudent() {
		
		//All variables below are used for GUI(Javafx)
		final Label addStudentLabel = new Label("Add Student");
		
	    final BorderPane addStudPane = new BorderPane();
	    final BorderPane addStudTitlePane = new BorderPane();
	    final Scene addStudScene= new Scene(addStudPane, 450, 230);
	    final BorderPane aSBP = new BorderPane();
	    final GridPane addStudGP = new GridPane();
	    final HBox addStudHB = new HBox(25);
	    final TextField firstNameTF = new TextField();
	    final TextField lastNameTF = new TextField();
	    final Stage addStudStage = new Stage();
	    final Button addOK = new Button("ADD");
		final Button addCancel = new Button("Cancel");
		
		//Simple array created to fill combo box with integers to avoid errors or mismatch input
		ComboBox<Integer> cbAge= new ComboBox<Integer>();
		
			for(int i=1;i<=100;i++){
            cbAge.getItems().add(i);
              }
		
			//formatting for window
			addStudHB.setAlignment(Pos.CENTER);
			addStudHB.setPadding(new Insets(10,10,40,10));
			addStudentLabel.setFont(new Font("Sans Serif", 25));
			addStudGP.setPadding(new Insets(15,15,10,15));
			aSBP.setCenter(addStudentLabel);
			aSBP.setAlignment(addStudentLabel,Pos.CENTER);
			aSBP.setPadding(new Insets(10));
			addStudPane.setPadding(new Insets(20,20,20,20));
			addStudGP.setHgap(15);
			addStudGP.setVgap(10);
			addStudGP.add(new Label("First Name:"), 0, 0);
			addStudGP.add(firstNameTF,0 , 1);
			addStudGP.add(new Label("Last Name:"), 1, 0);
			addStudGP.add(lastNameTF,1 , 1);
			addStudGP.add(new Label("Age:"), 2, 0);
			addStudGP.add(cbAge,2 , 1);
			addStudPane.setTop(aSBP);
			addStudPane.setCenter(addStudGP);
			addStudHB.getChildren().add(addOK);
			addStudHB.getChildren().add(addCancel);
			addStudPane.setBottom(addStudHB);
			addStudStage.setTitle("Add Student");
			addStudStage.setScene(addStudScene);
			addStudStage.show();
			
			//event handler for add button
			addOK.setOnAction( e ->
			{			
				PreparedStatement stmt = null;
				
					try {
						// retrieving text from text box and storing them into variables
						String firstName = firstNameTF.getText();
						String lastName= lastNameTF.getText();
						int ageStu= cbAge.getValue();
						
						//SQL query to insert new student into student list in our HotSchool Database
						String insertSQL = " INSERT INTO Student (firstName, lastName, age)" + "VALUES (?, ?, ?)";
						
						try {		
							stmt = conn.prepareStatement(insertSQL);
							stmt.setString(1, firstName);
							stmt.setString(2, lastName);
							stmt.setInt(3, ageStu);
							System.out.println("query prepared");
							this.studentList.getStudentList().clear();
							readFromDb();
						} catch (SQLException q) {
							q.printStackTrace();
							System.out.println("Unsucessful");
						}
						
						int numRowsAff = 0;
						try {
							numRowsAff = stmt.executeUpdate();
							System.out.println("Number of rows affected" + numRowsAff);
							Alert alert = new Alert(AlertType.CONFIRMATION);   
							alert.setTitle("Student Added");
				            alert.setContentText(firstName + " " + lastName + " has been added to the list");
				            alert.showAndWait();
				            addStudStage.close();
				            try {
								stmt.close();
							} catch (SQLException s) {
								// TODO Auto-generated catch block
								s.printStackTrace();
								}
						} catch(SQLException l) {
							l.printStackTrace();
							System.out.println("Error excuting query");
						}
					//if any values are not entered, this exception will be caught and the user will be notified
					} catch(NullPointerException b) {
						Alert alert = new Alert(AlertType.ERROR);                            
			            alert.setTitle("Student NOT Added");
			            alert.setHeaderText("Student NOT Added");
			            alert.setContentText("Please enter all values");
			            alert.showAndWait();
					}
					
					});
					
					addCancel.setOnAction(b -> {
						addStudStage.close();
					});

		}

	private void enrStudent () {
		// this method displays all students who are in Hotschool. To enroll a student, you must select them and 
		// click either enroll or unenroll
		final ListView<Student> studentLV = new ListView<>(FXCollections.observableArrayList());
		readFromDb();
		
		//setting listview with students currently in the student list
		studentLV.setItems(this.studentList.getStudentList());
		
		final BorderPane enrBPMn = new BorderPane();
		final Label enrLabel = new Label("Enroll/ Un-enroll a Student");
		final Label chStuLabel = new Label("Choose a student to enroll or un-enroll");
		final ScrollPane stLV= new ScrollPane(studentLV);
		final Button enroll = new Button ("Enroll");
		final Button unEnroll = new Button ("Un-enroll");
		final Button cancelEnr = new Button ("Cancel");
		final VBox enrVB = new VBox(20);
		final Stage enrStudStage = new Stage();
		
		//Formatting for window
		enrLabel.setFont(new Font("Sans Serif", 25));
		chStuLabel.setFont(new Font("Sans Serif", 15));
		stLV.prefHeightProperty().bind(enrBPMn.heightProperty().divide(2));
		BorderPane enrBP = new BorderPane();
		enrBP.setPadding(new Insets(20,0,20,0));
		enrVB.setPadding(new Insets(20,20,0,20));
		enrVB.getChildren().add(enroll);
		enrVB.getChildren().add(unEnroll);
		enrVB.getChildren().add(cancelEnr);
		Pane lvBP = new Pane();
		lvBP.getChildren().add(stLV);
		enrBP.setTop(enrLabel);
		enrBP.setCenter(chStuLabel);
		enrBP.setAlignment(chStuLabel, Pos.CENTER);
		enrBP.setAlignment(enrLabel, Pos.CENTER);
		enrBPMn.setTop(enrBP);
		enrBPMn.setCenter(lvBP);
		enrBPMn.setRight(enrVB);
		enrBPMn.setPadding(new Insets(20,20,20,20));
		enrBPMn.setAlignment(lvBP, Pos.CENTER);
		
		final Scene enrScene = new Scene(enrBPMn,400,350);
		
		studentLV.setItems(this.studentList.getStudentList());
		
        studentLV.getSelectionModel().selectedItemProperty().addListener( //controls main screen. if user selects song, information appears on screen
                ov -> {
                           for (Integer i : studentLV.getSelectionModel().getSelectedIndices()) {
                        	   this.s1=this.studentList.getStudentList().get(i);
                        	   enroll.setOnAction(e -> courseList(studentList.getStudentList().get(i)));
                        	   unEnroll.setOnAction(e -> remCourseList(studentList.getStudentList().get(i)));	   
                           }
                		});
        
        cancelEnr.setOnAction(b -> {
			enrStudStage.close();
		});
        
        enrStudStage.setScene(enrScene);
        enrStudStage.show();
                                    
	}
	
	private void courseList(Student student) {
		/*
		 * This method is used to enroll a student into a course //We take the
		 * student's course list everytime and clear it. The contents of the student
		 * course is read from the database using the addCourseToSl method
		 */		
		
		student.getStudentCL().getCourseList().clear();
		this.courseListEnr.getCourseList().clear();
		readCoursesFromDB();
		addCourseToSL();
		
		//All variables below are used for appearance, not functionality
		final ListView<Course> courseLV = new ListView<>(FXCollections.observableArrayList());
		final BorderPane courseenrBPMn = new BorderPane();
		final Label courseEnrLabel = new Label("Course List");
		final Label chCourseLabel = new Label("Choose a course to enroll a student in");
		final ScrollPane courseLV2= new ScrollPane(courseLV);
		final BorderPane courseBP = new BorderPane();
		final Button enroll = new Button ("Add Course");
		final Button cancelEnr = new Button ("Cancel");
		final VBox enrVB = new VBox(20);
		final Pane lvBP = new Pane();
		final Stage enrStudStage = new Stage();
		final Scene enrScene = new Scene(courseenrBPMn,400,350);
		
		//Styling for window
		courseEnrLabel.setFont(new Font("Sans Serif", 25));
		chCourseLabel.setFont(new Font("Sans Serif", 15));
		courseLV.prefHeightProperty().bind(courseenrBPMn.heightProperty().divide(2));
		courseBP.setPadding(new Insets(5,0,25,0));
		enrVB.setPadding(new Insets(70,20,0,20));
		enrVB.getChildren().add(enroll);
		enrVB.getChildren().add(cancelEnr);
		lvBP.getChildren().add(courseLV2);
		courseBP.setTop(courseEnrLabel);
		courseBP.setCenter(chCourseLabel);
		courseBP.setAlignment(chCourseLabel, Pos.CENTER);
		courseBP.setAlignment(courseEnrLabel, Pos.CENTER);
		courseenrBPMn.setTop(courseBP);
		courseenrBPMn.setCenter(lvBP);
		courseenrBPMn.setRight(enrVB);
		courseenrBPMn.setPadding(new Insets(20,20,20,20));
		courseenrBPMn.setAlignment(lvBP, Pos.CENTER);
		enrStudStage.setScene(enrScene);
		enrStudStage.show();

		// Here, we are adding the courses into our second courselist which is used just for appearence
		   for (int i = 0; i < this.courseList.getCourseList().size(); i++) {
			   	courseListEnr.add(this.courseList.getCourseList().get(i));
		  }
		   
		/*Created a loop to loop through the selected students course list, if the
		 * course is already in their course list, then we will remove ir from the list.
		 * This way, only courses that the students are not enrolled in will be
		 * displayed to avoid duplicate courses
		 */
		   
		   for (int a = 0; a < student.getStudentCL().getCourseList().size(); a++ ) {
			   for (int i = 0; i < this.courseListEnr.getCourseList().size(); i++) {
				   if (student.getStudentCL().getCourseList().get(a).getId() == courseListEnr.getCourseList().get(i).getId()) {
					   courseListEnr.getCourseList().remove(i);
					} else
						continue;
			   }
			     
		  }		   
		   
		//if the students are enrolled in all courses, then the user will no
		   if(this.courseListEnr.getCourseList().isEmpty()) {
			   Alert alert = new Alert(AlertType.CONFIRMATION);                            
	            alert.setTitle("Student sucessfully enrolled");
	            alert.setContentText(student.getFirstName() + " " + student.getLastName() + " is already enrolled in all courses") ;
	            alert.showAndWait();
	            enrStudStage.close();
		   } else {
		
		//adding students from list to listview
		courseLV.setItems(courseListEnr.getCourseList()); 
        courseLV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        courseLV.getSelectionModel().selectedItemProperty().addListener( //controls main screen. if user selects song, information appears on screen
                ov -> {
                           for (Integer i : courseLV.getSelectionModel().getSelectedIndices()) {
                        	   enroll.setOnAction(e -> {
                        		   
                        		   //SQL query used to enroll student into a course
                        		   String insertSQL = " INSERT INTO StudentCourse (studentID, courseID) VALUES (?, ?)";
                        			PreparedStatement stmt = null;
                        			
                        			try {		
                        				stmt = conn.prepareStatement(insertSQL);
                        				stmt.setInt(1, student.getStudentID());
                        				stmt.setInt(2, this.courseListEnr.getCourseList().get(i).getId());
                        				System.out.println("query prepared");
                        			} catch (SQLException q) {
                        				q.printStackTrace();
                        				System.out.println("Unsucessful");
                        			}
                        			
                        			int numRowsAff = 0;
                        			try {
                        				numRowsAff = stmt.executeUpdate();
                            				Alert alert = new Alert(AlertType.CONFIRMATION);                            
                            	            alert.setTitle("Student sucessfully enrolled");
                            	            alert.setContentText(student.getFirstName() + " " + student.getLastName() + " is now enrolled in " + this.courseListEnr.getCourseList().get(i).getCourseName());
                            	            alert.showAndWait().ifPresent(x -> {
                                                courseLV.getItems().clear();
                                                readFromDb();
                                        		readCoursesFromDB();
                                                addCourseToSL();
                                                courseLV.refresh();
                                                enrStudStage.close();
                            	            });
                    			
                        				System.out.println("Number of rows affected" + numRowsAff);
                        			} catch(SQLException w) {
                        				w.printStackTrace();
                        				System.out.println("Error excuting query");
                        			}
                        			
                        			try {
                						stmt.close();
                					} catch (SQLException s) {
                						// TODO Auto-generated catch block
                						s.printStackTrace();
                						}   
                        	   });
                           }
                });
                }
                
        
        cancelEnr.setOnAction(b -> {
			enrStudStage.close();
		});
        
	}
	
	private void remCourseList(Student student) {
		student.getStudentCL().getCourseList().clear();
		readCoursesFromDB();
		addCourseToSL();
		
		final ListView<Course> courseLV = new ListView<>(FXCollections.observableArrayList());
		final Label courseEnrLabel = new Label("Course List");
		final Label chCourseLabel = new Label("Choose a course to un-enroll a student from");
		final BorderPane courseenrBPMn = new BorderPane();
		final Button enroll = new Button ("Remove Course");
		final Button cancelEnr = new Button ("Cancel");
		final Stage enrStudStage = new Stage();
		final Scene enrScene = new Scene(courseenrBPMn,400,350);
		final ScrollPane courseLV2= new ScrollPane(courseLV);
		final BorderPane courseBP = new BorderPane();
		final VBox enrVB = new VBox(20);
		final Pane lvBP = new Pane();
	
		courseEnrLabel.setFont(new Font("Sans Serif", 25));
		chCourseLabel.setFont(new Font("Sans Serif", 15));
		courseLV.prefHeightProperty().bind(courseenrBPMn.heightProperty().divide(2));
		courseBP.setPadding(new Insets(5,0,25,0));
		enrVB.setPadding(new Insets(50,0,0,20));
		enrVB.getChildren().add(enroll);
		enrVB.getChildren().add(cancelEnr);
		lvBP.getChildren().add(courseLV2);
		courseBP.setTop(courseEnrLabel);
		courseBP.setCenter(chCourseLabel);
		courseBP.setAlignment(chCourseLabel, Pos.CENTER);
		courseBP.setAlignment(courseEnrLabel, Pos.CENTER);
		courseenrBPMn.setTop(courseBP);
		courseenrBPMn.setCenter(lvBP);
		courseenrBPMn.setRight(enrVB);
		courseenrBPMn.setPadding(new Insets(20,20,20,20));
		courseenrBPMn.setAlignment(lvBP, Pos.CENTER);
		enrStudStage.setScene(enrScene);
		enrStudStage.show();
		
		//if the student isn't enrolled in any courses, a message will display
		if(student.getStudentCL().getCourseList().isEmpty()) {
    				
    				Alert alert = new Alert(AlertType.ERROR); 
    	            alert.setHeaderText("Student is not enrolled in any courses");
    	            alert.setContentText("Please choose another student");
    	            alert.showAndWait();
    	            enrStudStage.close();
     		   
     		   
		} else {
		
		courseLV.setItems(student.getStudentCL().getCourseList()); //adding students from list to listviewS
        courseLV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        courseLV.getSelectionModel().selectedItemProperty().addListener( //controls main screen. if user selects song, information appears on screen
                ov -> {
                           for (Integer i : courseLV.getSelectionModel().getSelectedIndices()) {
                        	   
                        	   this.c1 = courseLV.getSelectionModel().getSelectedItem();
                        	   	   
                        	   	enroll.setOnAction(e -> {
									// if student is enrolled in courses, they are able to delete using the query
									String insertSQL = " DELETE FROM StudentCourse WHERE (studentID = ? AND courseID = ?)";
									
                        			PreparedStatement stmt = null;
                        			
                        			try {		
                        				stmt = conn.prepareStatement(insertSQL);
                        				stmt.setInt(1, this.s1.getStudentID());
                        				stmt.setInt(2, this.c1.getId());
                        				student.getStudentCL().getCourseList().clear();
                        				
                        				System.out.println("query prepared");
                        			} catch (SQLException q) {
                        				q.printStackTrace();
                        				System.out.println("Unsucessful");
                        			}
                        			
                        			int numRowsAff = 0;
                        			try {
                        				
                        				numRowsAff = stmt.executeUpdate();
                        				
                        				System.out.println("Number of rows affected" + numRowsAff);
                                        Alert alert = new Alert(AlertType.CONFIRMATION);                            
                                        alert.setTitle("Student Sucessfully Enrolled");
                                        alert.setContentText(student.getFirstName() + " " + student.getLastName() + " is no longer enrolled in " + c1.getCourseName());
                                        alert.showAndWait().ifPresent(x -> {
                                        student.getStudentCL().getCourseList().clear();
                                        courseLV.getItems().clear();
                                		readCoursesFromDB();
                                        addCourseToSL();
                                        courseLV.refresh();
                                        enrStudStage.close();
                                        });
                                     
                        			} catch(SQLException w) {
                        				w.printStackTrace();
                        				System.out.println("Error excuting query");
                        			}
                        			
                        			try {
                						stmt.close();
                					} catch (SQLException s) {
                						// TODO Auto-generated catch block
                						s.printStackTrace();
                						}
                        	   });
                           }        
                });
		}
                     
	}
	
	private void checkCourses() {
		
		BorderPane checkCourseBp = new BorderPane();
		Button search = new Button("search");
		addCourseToSL();
		
		//All variables listed below   used for appearance(GUI), not functionality
		final Label checkCoursesLabel = new Label("View Students in Course");
		final Label chCourseLabel = new Label("Enter name of course to see which students are enrolled");
		final VBox chckCoursesTp = new VBox(10);
		final BorderPane chCoursesGP = new BorderPane();
		final TextField courseName= new TextField();
		final BorderPane taPane = new BorderPane();
		final TextArea taCourses = new TextArea();
		final Label coName= new Label("  Course Name  ");
		// Styling for window
		checkCoursesLabel.setFont(new Font("Sans Serif", 20));
		chCourseLabel.setFont(new Font("Sans Serif", 14));
		chckCoursesTp.getChildren().add(checkCoursesLabel);
		chckCoursesTp.getChildren().add(chCourseLabel);
		chckCoursesTp.setPadding(new Insets(20));
		taCourses.setEditable(false);
		taPane.setCenter(taCourses);	
		taPane.setPadding(new Insets(20));
		taPane.prefWidthProperty().bind(checkCourseBp.widthProperty().divide(2));
		taPane.prefHeightProperty().bind(checkCourseBp.heightProperty().divide(2));
		chCoursesGP.setLeft(coName);
		chCoursesGP.setCenter(courseName);
		chCoursesGP.setRight(search);
		chCoursesGP.setPadding(new Insets(0,20,0,0));
		courseName.prefWidthProperty().bind(checkCourseBp.widthProperty().divide(10));
		chCoursesGP.setAlignment(coName, Pos.CENTER_LEFT);
		chCoursesGP.setAlignment(search, Pos.CENTER_RIGHT);
		chckCoursesTp.setAlignment(Pos.CENTER);
		checkCourseBp.setTop(chckCoursesTp);
		checkCourseBp.setCenter(chCoursesGP);
		checkCourseBp.setBottom(taPane);
		checkCourseBp.setAlignment(taPane, Pos.CENTER_RIGHT);
		
		Scene chckCourseScene = new Scene(checkCourseBp,550,350);
		Stage chckCourseStage = new Stage();
		
		search.setOnAction(a-> {
			String courses="";
			taCourses.clear();
			// SQL query used to display the first name and last name of students enrolled in a course
			String selectSQL ="SELECT firstName, lastName FROM student " +
					"INNER JOIN studentcourse ON studentcourse.studentID = student.id " +
					"INNER JOIN course ON studentcourse.courseID = course.id " + 
					"WHERE courseName = ? ";
			
			String courseName2= courseName.getText();
			PreparedStatement stmt2 = null;
			
			try {
				stmt2 = conn.prepareStatement(selectSQL);
				stmt2.setString(1, courseName2);
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Unsuccesful");
			}
			
			ResultSet rs = null;
			
			try {
				rs = stmt2.executeQuery();
			} catch (SQLException e){
				e.printStackTrace();
				System.out.println("Unsucessful Query");
			}
			
			try {
				while(rs.next()) {
					String firstName = rs.getString("firstName");
					String lastName = rs.getString("lastName");
					
					courses+= firstName + " " + lastName +"\n";
				}
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Unsuccessul Query");
			}	//if the string is empty, then there are no students enrolled. This will be displayed below
				if(courses.isEmpty()) {
					taCourses.setText("There are no students enrolled in this course");
				} else
				taCourses.setText(courses);
				
				try {
					stmt2.close();
					rs.close();
				} catch (SQLException a1) {
					a1.printStackTrace();
				}
	
			});

		chckCourseStage.setTitle("Student Courses");
		chckCourseStage.setScene(chckCourseScene);
		chckCourseStage.show();
		
	}
	
	private void checkStudentCourses() {
		
		//All variables listed below are used for appearance purposes only
		final BorderPane checkStudBPMn2 = new BorderPane();
		final Label checkStuLabel = new Label("View Courses a Student is Enrolled in");
		final Label chStuLabel = new Label("Enter name of student to see which courses they are enrolled in");
		final VBox chckStudTp = new VBox(20);
		final GridPane chStuGP = new GridPane();
		final TextField fName= new TextField();
		final TextField lName= new TextField();
		final BorderPane taPane = new BorderPane();
		final TextArea taCourses = new TextArea();
		final Button search = new Button("Search");
		
		//Styling for window
		checkStuLabel.setFont(new Font("Sans Serif", 20));
		chStuLabel.setFont(new Font("Sans Serif", 14));
		chckStudTp.getChildren().add(checkStuLabel);
		chckStudTp.getChildren().add(chStuLabel);
		chckStudTp.setPadding(new Insets(20));
		chStuGP.setPadding(new Insets(10));
		taCourses.setEditable(false);
		taPane.setCenter(taCourses);	
		taPane.setPadding(new Insets(20));
		fName.prefWidthProperty().bind(chStuGP.widthProperty().divide(7));
		lName.prefWidthProperty().bind(chStuGP.widthProperty().divide(7));
		taPane.prefWidthProperty().bind(checkStudBPMn2.widthProperty().divide(2));
		taPane.prefHeightProperty().bind(checkStudBPMn2.heightProperty().divide(2));
		chStuGP.add((new Label("          First Name  ")), 0, 0);
		chStuGP.add(fName, 1, 0);
		chStuGP.add((new Label("  Last Name  ")), 2, 0);
		chStuGP.add(lName, 3, 0);
		chStuGP.add(search, 5, 0);
		chStuGP.setHgap(9);
		chckStudTp.setAlignment(Pos.CENTER); 
		checkStudBPMn2.setTop(chckStudTp);
		checkStudBPMn2.setCenter(chStuGP);
		checkStudBPMn2.setBottom(taPane);
		checkStudBPMn2.setAlignment(taPane, Pos.CENTER_RIGHT);
		checkStudBPMn2.setAlignment(chStuGP, Pos.CENTER);
			
		search.setOnAction(a-> {
			PreparedStatement stmt2 = null;
			ResultSet rs = null;
			
			String courses="";
			taCourses.clear();
			
			//SQL Query used to select courses that students are enrolled in
			String selectSQL ="SELECT courseName " +
					"FROM student " +
					" INNER JOIN studentcourse" +
					" ON studentcourse.studentID = student.id" +
					" INNER JOIN course" +
					" ON studentcourse.courseID = course.id" +
					" WHERE firstName= ? AND lastName =?";
			
			String firstName= fName.getText();
			String lastName = lName.getText();
			
			try {
				stmt2 = conn.prepareStatement(selectSQL);
				stmt2.setString(1, firstName);
				stmt2.setString(2, lastName);
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Unsuccesful");
			}
			
			try {
				rs = stmt2.executeQuery();
			} catch (SQLException e){
				e.printStackTrace();
				System.out.println("Unsucessful Query");
				
			}
			
			try {
				while(rs.next()) {
					String courseName = rs.getString("courseName");
					
					courses+= courseName +"\n";
				}
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Unsuccessul Query");
				
			}	//If student isnt enrolled in any courses, a message will display
				if(courses.isEmpty()) {
					taCourses.setText(firstName + " " + lastName + " is not enrolled in any courses");
				} else
				taCourses.setText(courses);
				
				try {
					stmt2.close();
					rs.close();
				} catch (SQLException a1) {
					a1.printStackTrace();
				}

			});
				
		Scene chckStu = new Scene(checkStudBPMn2,500,300);
		Stage chStuStg = new Stage();
		chStuStg.setTitle("Student Courses");
		chStuStg.setScene(chckStu);
		chStuStg.show();
			
	}

	private void connectToDB (String connURL, String user, String pass) {
		
		//This method is used to connect to the HotSchool database
		try {
			conn = DriverManager.getConnection(connURL, user, pass);
			System.out.println("CONNNECTED!");
		} catch(SQLException e) {
			System.out.println("Error getting connection!");
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.out.println(e.getSQLState());
			System.out.println(e.getErrorCode());
		}
		
	}
	
	private void readFromDb() {
		//This method is used to read all contents from student list to the student list array
		this.studentList.getStudentList().clear();
		String selectSQL = "SELECT * FROM Student";
		
		PreparedStatement stmt2 = null;
		
		try {
			stmt2 = conn.prepareStatement(selectSQL);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Unsuccesful");
		}
		
		ResultSet rs = null;
		
		try {
			rs = stmt2.executeQuery();
		} catch (SQLException e){
			e.printStackTrace();
			System.out.println("Unsucessful Query");
			
		}
		//If there are records, they will be added to the student list used in the studentList
		try {
			while(rs.next()) {
				int id= rs.getInt("id");
				String fname = rs.getString("firstName");
				String lname = rs.getString("lastName");
				int age = rs.getInt("age");
				
				this.studentList.add(new Student(id,fname,lname,age));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Unsuccessul Query");
		}
	}
	
	private void readCoursesFromDB() {
		//This method is used to read all contents from course list to the course list array
		this.courseList.getCourseList().clear();
		String selectSQL = "SELECT * FROM Course";
		
		PreparedStatement stmt2 = null;
		
		try {
			stmt2 = conn.prepareStatement(selectSQL);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Unsuccesful");
		}
		
		ResultSet rs = null;
		
		try {
			rs = stmt2.executeQuery();
		} catch (SQLException e){
			e.printStackTrace();
			System.out.println("Unsucessful Query");
			
		}
		
		try {
			while(rs.next()) {
				int id= rs.getInt("id");
				String courseName = rs.getString("courseName");
				String courseTime = rs.getString("startTime");
				
				this.courseList.add(new Course(id,courseName, courseTime));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Unsuccessul Query");
		}
		
		
	}
	
	public void addCourseToSL() {
		//using the student course table to add courses to the student's course list
		String selectSQL = "SELECT * FROM StudentCourse";
		
		PreparedStatement stmt2 = null;
		
		try {
			stmt2 = conn.prepareStatement(selectSQL);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Unsuccesful");
		}
		
		ResultSet rs = null;
		
		try {
			rs = stmt2.executeQuery();
		} catch (SQLException e){
			e.printStackTrace();
			System.out.println("Unsucessful Query");
			
		}
		
		try {
			while(rs.next()) {
				int studentID= rs.getInt("studentId");
				int courseID= rs.getInt("courseId");
				
				for(int i=0; i<this.studentList.getStudentList().size(); i++) {
					if(this.studentList.getStudentList().get(i).getStudentID() == studentID) {
						for(int i1=0; i1<this.courseList.getCourseList().size(); i1++) {
							if(this.courseList.getCourseList().get(i1).getId() == courseID) {
								this.studentList.getStudentList().get(i).getStudentCL().getCourseList().add(this.courseList.getCourseList().get(i1));
							}
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Unsuccessul Query");
		} 
		try {
			rs.close();
			stmt2.close();	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		
		launch(args);
		
	}
}
