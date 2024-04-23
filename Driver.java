import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.*;
import java.io.Serializable;
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class Driver {
  private static Scanner scanner = new Scanner(System.in);
  public static void main(String[] args) {
    //initializing the course array with values from lect
    File lect = new File("lect.txt");
    //System.out.println("File accpeted");
    CourseList courseList = new CourseList();
    //System.out.printf("File accpeted\n");
    fillCourseList(lect,  courseList);
    courseList.printList();
    
    System.out.println(courseList.getLab(30008));
     
     int choice;
          do {
              System.out.println("Main Menu\n1 : Student Management\n2 : Course Management\n0 : Exit");
              System.out.print("Enter your selection: ");
              choice = scanner.nextInt();
              switch (choice) {
                  case 1:
                    StudentManagementMenu();
                      break;
                  case 2:
                    CourseManagementMenu();
                      break;
                  case 0:
                      System.out.println("Exiting...");
                      break;
                  default:
                      System.out.println("Invalid choice. Please try again.");
              }
          } while (choice != 0);
    //courseList.deleteCourse(51180);
    //courseList.searchList(51180);
    //courseList.searchList(32658);
    courseList.deleteCourse(12658);
    closeFile(lect, courseList);
  }

  public static void CourseManagementMenu()
  {
    char courseChoice;
        Scanner courseScanner = new Scanner(System.in);
        do {
          System.out.println("\nCourse Management Menu\n\nChoose one of:\n\n  A - Search for a class or lab using the class/lab number\n  B - delete a class\n  C - Add a lab to a class\n  X - Back to main menu ");
          System.out.print("\n\nEnter your selection: ");
            //convert to uppercase directly after input
            courseChoice = courseScanner.next().toUpperCase().charAt(0);
            switch (courseChoice) { //char switch statement, doesnt crash when inputting integers
            case 'A':
              System.out.print("Class/lab search function \n");
                break;
            case 'B':
              System.out.print("Delete Function \n");
                break;
            case 'C':
                System.out.println("Lab add\n");
                break;
            case 'X':
               System.out.println("Back to main menu");
               break;
            default:
                System.out.println("Invalid choice. Please try again.\n");
            }
        } while (courseChoice != 'X');

    }

  public static void StudentManagementMenu()
  {
      char studentChoice;
    try (Scanner studentScanner = new Scanner(System.in)) {
          do {
            System.out.println("\nCourse Management Menu\n\nChoose one of:\n\n  A - Search for a class or lab using the class/lab number\n  B - delete a class\n  C - Add a lab to a class\n  X - Back to main menu ");
              System.out.print("\n\nEnter your selection: ");
              //convert to uppercase directly after input
              studentChoice = studentScanner.next().toUpperCase().charAt(0);
              switch (studentChoice) { //char switch statement, doesnt crash when inputting integers
            case 'A':
              System.out.print("Student search\n");
                break;
            case 'B':
              System.out.print("Delete student\n");
                break;
            case 'C':
                System.out.println("Print fee invoice\n");
                break;
            case 'D':
                System.out.println("Print list of students\n");
                break;
            case 'X':
               System.out.println("Back to main menu");
               break;
            default:
                System.out.println("Invalid choice. Please try again.\n");
              }		
          } while (studentChoice != 'X');	
    }
  }

  //fills courselist with courses from inputed file
  public static void fillCourseList(File lect, CourseList courseList){
    String tmpLine = null;
    String [] splitLine = null;
    Course newCourse = null;
    Course prevCourse = null; //stores the previous course so labs can be added to it

    Scanner fileScanner = openFile(lect);

    //loops through whole file
    while(fileScanner.hasNext() == true) {
      tmpLine = fileScanner.nextLine();
      splitLine = tmpLine.split(",");


      int len = splitLine.length;

      //when len is 8 that means a course that takes place irl is being added
      if(len == 8) {
        newCourse = new Course(Integer.parseInt(splitLine[0]), splitLine[1], splitLine[2], splitLine[3], splitLine[4], splitLine[5], splitLine[6], Integer.parseInt(splitLine[7].trim()));
        courseList.addCourse(newCourse);
        prevCourse = newCourse;
      }
      //when len is 6 an online course is being added
      else if(len == 6) {
        newCourse = new Course(Integer.parseInt(splitLine[0]), splitLine[1], splitLine[2], splitLine[3], splitLine[4], Integer.parseInt(splitLine[5].trim()));
        courseList.addCourse(newCourse);
        prevCourse = newCourse;
      }
      //when len is 2 a lab is being added to the previous course
      else if(len == 2) {
        Lab newLab = new Lab(Integer.parseInt(splitLine[0]), splitLine[1]);
        prevCourse.addLab(newLab);

      }
      else {
        System.out.println("Invalid file input");
      }


    }

    return;
  }

  //returns a scanner linked to the inputed file
  public static Scanner openFile(File lect){
    Scanner fileScanner = null; 

    //trys to open lec.txt file
    try {
      fileScanner = new Scanner(lect);
    } catch (FileNotFoundException e) {
      System.out.println("file not found");
      System.exit(0);
    }
    return fileScanner;

  }
  
  //prints to file all courses and labs not including the disabled ones
  public static void closeFile(File lect, CourseList courseList) {
	  PrintWriter writer = null;
	  try {
		  writer = new PrintWriter(lect);
		  ArrayList <Course> list = courseList.getList();
		  for(Course course : list) {
			  if(course.getEnabled()) {
				  if((course.getModality()).compareToIgnoreCase("online") == 0) { //check if its an online class as it doesnt have to print if it has labs or its location
					  writer.printf("%d,%s,%s,%s,%s,%d\n", course.getCrn(), course.getPrefix() , course.getTitle(), course.getClassLevel(), course.getModality(), course.getCreditHours());
				  }
				  else {//if its not online it has to print out all of its fields
					  writer.printf("%d,%s,%s,%s,%s,%s,%s,%d\n", course.getCrn(), course.getPrefix() , course.getTitle(), course.getClassLevel(), course.getModality(), course.getLocation(), course.getHasLab(), course.getCreditHours());
				  }
				  if(course.hasLabs()) { //checks if course has labs and if it does it gets labs
					  ArrayList <Lab> labList = course.getLabList();
					  for(Lab lab : labList) {//prints all labs to file that are enabled
						  if(lab.getEnabled()) {
							  writer.printf("%d,%s\n", lab.getCrn(), lab.getLocation());
						  }
					  }
				  }
			  }
		  }
	  }
	  catch(Exception e) {
		  System.out.println("File could not be closed");
		  System.exit(0);
	  
	  }
	  finally {
		  if(writer != null) {
			  writer.close();
		  }
	  }

  }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class CourseList{
  private ArrayList <Course> list;


  public CourseList() {
    list = new ArrayList <Course>();
  }
  
  public ArrayList <Course> getList() {
	  return list;
  }

  public void addCourse(Course course) {
    list.add(course);
  }

  

  //search for a lab or course through there crn and prints it
  public void searchList(int crn){
    for(Course course : list) {
      //if crn matches and course is enabled course is printed out
      if((course.getCrn() == crn) && (course.getEnabled() == true)) {

    	System.out.printf("[ %d,%s,%s ]\n\n", course.getCrn(), course.getPrefix(), course.getTitle());
        return;
      }
      //goes through any labs under that course and if they match crn and its enabled, it prints out the course the lab is for and then the lab
      if(course.hasLabs()) {
        Lab tmpLab = course.getSpecificLab(crn); //checks all the labs under a course if they match the crn, if it does it fetchs the lab, otherwise its null

        if(tmpLab != null) {
          System.out.printf("Lab for [ %d,%s,%s ]\n", course.getCrn(), course.getPrefix(), course.getTitle());
          System.out.printf("Lab Room %s\n\n", tmpLab.getLocation());
          return;
        }
      }
    }
    System.out.println("Course/Lab is not available: invalid crn\n");
  }


  public void printList() {
    for(Course course : list){
      System.out.println(course);
    }
    System.out.println();
  }


  //disables whichever course or lab crn represents
  public void deleteCourse(int crn) {
    //loops through all courses
    for(Course course : list) {
      //checks if an enabled course matches crn
      if((course.getCrn() == crn) && (course.getEnabled() == true) && (course.Empty())) {
        //disables course if it doesnt have labs
        if(!course.hasLabs()) {
          course.setEnabled(false);
          System.out.printf("[ %d,%s,%s ] deleted!\n\n",course.getCrn(), course.getPrefix(), course.getTitle());
          return;
        }
        //disables course if it has labs and there all empty
        if(course.allLabsEmpty()) {
          course.setEnabled(false);
          course.allLabsSetEnabled(false);
          System.out.printf("[ %d,%s,%s ] deleted!\n\n",course.getCrn(), course.getPrefix(), course.getTitle());
          return;
        }
        System.out.println("course or its labs are not empty so it cant be deleted");
      }


      //checks all labs under a course and if one matches the crn and is empty it is disabled
      if(course.hasLabs()) {
        Lab tmpLab = course.getSpecificLab(crn);
        if(tmpLab != null) {
          if(tmpLab.empty()) {
            tmpLab.setEnabled(false);
            System.out.printf("Lab %d %s deleted!\n\n",tmpLab.getCrn(), tmpLab.getLocation() );
            return;
          }
          else {
            System.out.println("lab is not empty so it cant be deleted");
            return;
          }
        }
      }
    }
    System.out.printf("Class could not be found: invalid crn\n\n");
  }
  
  //gets course if its exists otherwise returns null
  public Course getCourse(int crn) {
	  int len = list.size();
	  for(int i = 0; i < len; i++) {
		  if(list.get(i).getCrn() == crn) {
			  return list.get(i);
		  }
	  }
	  return null; //returns null as crn wasn't found
	 
  }
  
  //gets course if its exists otherwise returns null
  public Course getCourse(String preFix) {
	  int len = list.size();
	  for(int i = 0; i < len; i++) {
		  if((list.get(i).getPrefix()).compareToIgnoreCase(preFix) == 0) {
			  return list.get(i);
		  }
	  }
	  return null; //returns null as prefix wasn't found
	 
  }
  	//gets lab if its valid otherwise returns null
	public Lab getLab(int crn) {
		for(Course course : list) {
			if(course.hasLabs()) {
				Lab lab = course.getSpecificLab(crn); //if there is lab with matching crn it is returned otherwise its null
				if(lab != null) {
					return lab; // returns found lab
				}	
			}
		}
		return null; //couldnt find a lab with crn
	}
}




////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class Course {
  private int crn;
  private String prefix;
  private String title;
  private String classLevel; // Undergraduate or Graduate
  private String modality;
  private String location;
  private String hasLab; //Yes or No
  private ArrayList <Lab> labList; //stores an arraylist of labs but if there are no labs it = null
  private int creditHours;
  private boolean enabled; //class is true by default, but is set to false when class has been "deleted"
  private int numPeopleTakingCourse; //keeps track of number of people taking the course, should be incremented and decremented when people add or drop a course


  //constructor for irl classes, labs are added after
  public Course(int crn, String prefix, String title, String classLevel, String modality, String location, String hasLab, int creditHours) {
    super();
    this.crn = crn;
    this.prefix = prefix;
    this.title = title;
    this.classLevel = classLevel;
    this.modality = modality;
    this.location = location;
    this.hasLab = hasLab;
    this.creditHours = creditHours;
    this.enabled = true;
    this.numPeopleTakingCourse = 0;

    //if haslab = Yes then it initializes an array list but otherwise the list is null
    if(hasLabs()) {
      this.labList = new ArrayList<Lab>();
    }
    else {
      this.labList = null;
    }
  }

  //constructor for online courses
  public Course(int crn, String prefix, String title, String classLevel, String modality, int creditHours) {
    super();
    this.crn = crn;
    this.prefix = prefix;
    this.title = title;
    this.classLevel = classLevel;
    this.modality = modality;
    this.location = null;
    this.hasLab = "No";
    this.creditHours = creditHours;
    this.labList = null;
    this.enabled = true;
    this.numPeopleTakingCourse = 0;
  }

  @Override
  public String toString() {
    if(!enabled) {
      return "this course is disabled";
    }


    if(hasLabs()) {
      return "lecture [" + crn + ", " + prefix + ", " + title + ", " + classLevel
        + ", " + modality + ", " + location + ", " + hasLab + ", " 
        + creditHours + ", " + numPeopleTakingCourse + "]" + "\n" + labList;
    }
    else return "lecture [" + crn + ", " + prefix + ", " + title + ", " + classLevel
        + ", " + modality + ", " + location + ", " + hasLab + ", " 
        + creditHours + ", " + numPeopleTakingCourse + "]";
  }

  // returns true or false if course has a lab
  public boolean hasLabs() {
    if((hasLab.toLowerCase()).compareTo("yes") == 0) {
      return true;
    }
    else {
      return false;
    }
  }

  //gets a lab based on its crn number and if that crn isn't in the list it returns null
  public Lab getSpecificLab(int crn){
    //checks to make sure it has labs
    if(!hasLabs()) {
      return null;
    }
    int len = labList.size();

    //seaches labList for an enabled lab with the matching crn
    for(int i = 0; i < len; i++) {
      if((labList.get(i).getCrn()) == crn && (labList.get(i).getEnabled() == true)) {
        return labList.get(i);
      }
    }
    return null;
  }

  //adds a lab to course 
  public void addLab(Lab lab) {
    if(hasLabs()) {
      labList.add(lab);
      return;
    }
    else {
      System.out.println("hasLab is set to no for this course");
      return;
    }
  }

  //returns true if no one is taking the course, otherwise false
  public boolean Empty(){
    if(numPeopleTakingCourse == 0) {
      return true;
    }
    else return false;
  }

  //returns true if all labs for course are empty, returns false if there are no labs for class or labs arent empty
  public boolean allLabsEmpty(){
    if(!hasLabs()) {
      return false;
    }

    //checks all labs to see if there empty
    for(Lab lab : labList) {
      if(!lab.empty()) {
        return false;
      }
    }
    return true;

  }

  public void allLabsSetEnabled(boolean val) {
    for(Lab lab : labList) {
      lab.setEnabled(val);
    }
    return;
  }

  public Lab getLab(int index) {
    if(labList.size() <= index) {
      System.out.println("labIndex is not valid");
      return null;
    }
    return labList.get(index);
  }

  public int getLabListLen() {
    return labList.size();
  }


  public boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(boolean val) {
    enabled = val;
  }

  public int getCrn(){
    return crn;
  }


  public String getPrefix() {
    return prefix;
  }

  public String getTitle() {
    return title;
  }

  public String getClassLevel() {
    return classLevel;
  }

  public String getModality() {
    return modality;
  }

  public String getLocation() {
    return location;
  }

  public int getCreditHours() {
    return creditHours;
  }
  
  public ArrayList <Lab> getLabList() {
	  return labList;
  }

  public String getHasLab() {
	  return hasLab;
  }
  
}

class Lab {
  private int crn;
  private String location;
  private boolean enabled; //class is true by default, but is set to false when class has been "deleted"
  private int numPeopleTakingLab; //keeps track of number of people taking the lab, should be incremented and decremented when people add or drop a lab

  public Lab(int crn, String location) {
    super();
    this.crn = crn;
    this.location = location;
    this.enabled = true;
    this.numPeopleTakingLab = 0;

  }

  @Override
  public String toString() {
    if(enabled) {
      return "Lab " + crn + " " + location + " " + numPeopleTakingLab + "  ";
    }
    return "Lab is disabled";
  }

  //returnsw true if lab 
  public boolean empty() {
    if(numPeopleTakingLab == 0) {
      return true;
    }
    else {
      return false;
    }
  }

  public int getCrn(){
    return crn;
  }
  public String getLocation() {
    return location;
  }

  public boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(boolean val) {
    enabled = val;
  }



}

//student superclass and subclasses start here //////////////////

abstract class Student
{
  private String name;
  private int id;

  public Student (String name, int id)
  {
    this.name = name;
    this.id = id;
  }

  //overwritten in subclasses
  abstract public void printInvoice();
  
  //getters (might be unneccessary?)
  public String getName()
  {
    return this.name;
  }
  public int getId()
  {
    return this.id;
  }
}

class UndergraduateStudent extends Student
{
  private String courses; //string array might be better since multiple courses?

  public UndergraduateStudent (String name, int id, String courses)
  {
    super (name, id);
    this.courses = courses;
  }
  
  public void printInvoice()
  {
    System.out.println("UGRAD INVOICE WILL PRINT HERE");
  }
}

abstract class GraduateStudent extends Student
{
  public GraduateStudent(String name, int id)
  {
    super(name, id);
  }
}

class MsStudent extends GraduateStudent
{
  private String courses; //string array might be better since multiple courses?
  
  public MsStudent(String name, int id, String courses)
  {
    super (name, id);
    this.courses = courses;
  }
  
  public void printInvoice()
  {
    System.out.println("MS INVOICE WILL PRINT HERE");
  }
}

class PhdStudent extends GraduateStudent
{
  private String advisor;
  private String subject;
  private int[] labCrns; //int array since phd can supervise multiple labs, not sure if String or normal int would be better?

  public PhdStudent(String name, int id, String advisor, String subject, int[] labCrns)
  {
    super(name, id);
    this.advisor = advisor;
    this.subject = subject;
    this.labCrns = labCrns;
  }

  public void printInvoice()
  {
    System.out.println("PHD INVOICE WILL PRINT HERE");
  }
}

//exception class here

class IdException extends Exception implements Serializable 
{
  private static final long serialVersionUID = 1L;
  public String excMsg()
  {
    return "Invalid ID format. Must be 'xx0000'";
    //to be thrown in a try block after checking that an ID is wrong format or is a duplicate
  }
}
