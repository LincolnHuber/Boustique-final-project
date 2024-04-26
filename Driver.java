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

    

   
    //all students will be stored here in case other parts of the program need to access, if there is an issue with them being in main pls lmk
    ArrayList<Student> studentList = new ArrayList<Student>();
  
     int choice;
          do {
              System.out.println("Main Menu\n1 : Student Management\n2 : Course Management\n0 : Exit");
              System.out.print("Enter your selection: ");
              choice = scanner.nextInt();
              switch (choice) {
                  case 1:
                    StudentManagementMenu(courseList, studentList);
                      break;
                  case 2:
                    CourseManagementMenu(courseList);
                      break;
                  case 0:
                      System.out.println("Exiting...");
                      break;
                  default:
                      System.out.println("Invalid choice. Please try again.");
              }
          } while (choice != 0);
    
    closeFile(lect, courseList);
  }

  public static void CourseManagementMenu(CourseList list)
  {
    char courseChoice;
    	int crn = 0;
    	String location = null;
        Scanner courseScanner = new Scanner(System.in);
        do {
          System.out.println("\nCourse Management Menu\n\nChoose one of:\n\n  A - Search for a class or lab using the class/lab number\n  B - delete a class\n  C - Add a lab to a class\n  X - Back to main menu ");
          System.out.print("\n\nEnter your selection: ");
            //convert to uppercase directly after input
            courseChoice = courseScanner.next().toUpperCase().charAt(0);
            switch (courseChoice) { //char switch statement, doesnt crash when inputting integers
            case 'A'://searchs for a course or lab
              System.out.print("Class/lab search function \n");
              System.out.print("Enter crn: ");
              crn = Integer.parseInt(courseScanner.next());
              list.searchList(crn);
                break;
            case 'B'://deletes a course
              System.out.print("Delete Function \n");
              System.out.print("Enter crn: ");
              crn = Integer.parseInt(courseScanner.next());
              list.deleteCourse(crn);
                break;
            case 'C'://adds a lab to a course
              System.out.println("Lab add\n");
              System.out.print("Enter crn for course you want to add lab to: ");
              crn = Integer.parseInt(courseScanner.next());
              Course tmpCourse = list.getCourse(crn);
              if(tmpCourse != null) { //A valid crn was inputed
            	  System.out.print("Enter lab's crn: ");
            	  crn = Integer.parseInt(courseScanner.next());
            	  System.out.print("Enter lab's location: ");
            	  location = courseScanner.next();
            	  tmpCourse.addLab(new Lab(crn, location));
              }
              else {//course could not be found for inputed crn
            	  System.out.println("Course could not be found: invalid crn\n");
              }
                break;
            case 'X':
               System.out.println("Back to main menu");
               break;
            default:
                System.out.println("Invalid choice. Please try again.\n");
            }
        } while (courseChoice != 'X');

    }

  public static void StudentManagementMenu(CourseList courseList, ArrayList<Student> studentList)
  {
    char studentChoice;
    String tempID;
    String tempType;
    String tempName;
    String tempCourses;
    String tempAdvisor;
    String tempSubject;
    int isResident = 0;

    Scanner charScan = new Scanner(System.in);
    Scanner stringScan = new Scanner(System.in); 
    Scanner intScan = new Scanner(System.in);
    Scanner resScan = new Scanner(System.in); //scanner used for undergrads to store if they are a resident or not
    //ik these are technically not necessary but it makes it simpler to me, if needed to change back to just one scanner lmk - Chris
    
    do 
    {
      System.out.println("\nStudent Management Menu\n\nChoose one of:\n\n  A - Add a student\n  B - Delete a student\n  C - Print invoice by student ID\n  D - Print all students\n  X - Back to main menu ");
      
      System.out.print("\n\nEnter your selection: ");
      
      //convert to uppercase directly after input
      studentChoice = charScan.next().toUpperCase().charAt(0);
      System.out.print("\n\n");
      
      switch (studentChoice) 
      { //char switch statement, doesnt crash when inputting integers
        case 'A': ////////////////////////////////ADD FUNCTION////////////////////////////////////////////
          System.out.print("Enter Student’s ID: ");
          tempID = stringScan.nextLine().toUpperCase(); //toUpper to make search easier
          
          if (checkID(tempID) == true) //checks if id is in correct format
          {
            System.out.println("VALID ID (see comment)\n"); //checkID() still needs to be updated to check if id is a duplicate
          }
          else
          { 
            try
            {
              throw new IdException();
            }
            catch (IdException e)
            {
              System.out.println(e.excMsg());
            }

            break;
          }
          
          System.out.print("Enter Student Type (PhD, MS or Undergrad): ");
          tempType = stringScan.nextLine().toUpperCase(); //toUpper to make search easier
          System.out.print("\n\n");

          if (tempType.equals("PHD")) //PHD ADD SECTION///////////////////////////////////
          {
            System.out.print("Enter Student Name: ");
            tempName = stringScan.nextLine();
            System.out.print("\n\n");

            System.out.print("Enter Advisor Name: ");
            tempAdvisor = stringScan.nextLine();
            System.out.print("\n\n");

            System.out.print("Enter Research Subject: ");
            tempSubject = stringScan.nextLine();
            System.out.print("\n\n");

            System.out.print("Enter Supervised Lab Courses Separated by Comma (Ex. 12345,67890): ");
            tempCourses = stringScan.nextLine();
            System.out.print("\n\n");
            String[] tempCourseList = tempCourses.split(","); //seperate labs into array

            ArrayList<Integer> tempLabList  = new ArrayList<Integer>();

            for(String test : tempCourseList) //converts string array to int array
            {
              tempLabList.add(Integer.parseInt(test));
            }

            for (int test : tempLabList) //verify that all input labs are valid
            {
              Lab testLab = courseList.getLab(test);
              //System.out.println(test); // <- prints the int getting passed to getCourse()
              if (testLab == null)
              {
                System.out.println("One or more labs were not valid");
                return;
              }
            }

            Student newStudent = new PhdStudent(tempName, tempID, tempAdvisor, tempSubject, tempLabList);
            studentList.add(newStudent);
          }
          else if (tempType.equals("MS")) //MS ADD SECTION///////////////////////////////////
          {
            System.out.print("Enter Student Name: ");
            tempName = stringScan.nextLine();
            System.out.print("\n\n");

            System.out.print("Enter Courses Separated by Comma (Ex. abc1234,def5678): ");
            tempCourses = stringScan.nextLine().toUpperCase();
            System.out.print("\n\n");
            String[] tempCourseList = tempCourses.split(","); //seperate courses into array

            for (String test : tempCourseList) //verify that all input courses are valid
            {
              Course testCourse = courseList.getCourse(test);
              //System.out.println(test); // <- prints the string getting passed to getCourse()
              
              if (testCourse == null || !(testCourse.isGraduate()))
              {
                System.out.println("One or more courses were not valid");
                return;
              }
              System.out.print(testCourse);
            }

	        //increments numPeopleTakingCourse field being stored in course
	        for(String test : tempCourseList) {
	        	courseList.getCourse(test).addPerson();
	        }
            
            Student newStudent = new MsStudent(tempName, tempID, tempCourseList);
            studentList.add(newStudent); //adding new stud to correct list
            
          }
          else if (tempType.equals("UNDERGRAD")) //UGRAD ADD SECTION///////////////////////////////////
          {
            System.out.print("Enter Student Name: ");
            tempName = stringScan.nextLine();
            System.out.print("\n\n");
            System.out.print("Is the student a resident? Enter 1 if yes or a 0 if no\n");
            isResident = resScan.nextInt();

            System.out.print("Enter Courses Separated by Comma (Ex. abc1234,def5678): ");
            tempCourses = stringScan.nextLine().toUpperCase();
            System.out.print("\n\n");
            String[] tempCourseList = tempCourses.split(","); //seperate courses into array
            for (String test : tempCourseList) //verify that all input courses are valid
            {
              Course testCourse = courseList.getCourse(test);
              //System.out.println(test); // <- prints the string getting passed to getCourse()

              if (testCourse == null || !testCourse.isUndergraduate())
              {
                System.out.println("One or more courses were not valid");
                return;
              }
            }
            
            //increments numPeopleTakingCourse field being stored in course
            for(String test : tempCourseList) {
            	courseList.getCourse(test).addPerson();
            }

            Student newStudent = new UndergraduateStudent(tempName, tempID, tempCourseList, isResident);
            studentList.add(newStudent);
          }
          else
          {
            System.out.println("Invalid Student Type\n");
            break;
          }
          
          break;
          
        case 'B': ////////////////////////////////DELETE FUNCTION////////////////////////////////////////////
          
          System.out.print("Enter Student’s ID: ");
          tempID = stringScan.nextLine().toUpperCase(); //toUpper to make search easier

          if (checkID(tempID) == true) //checks if id is in correct format
          {
            System.out.println("VALID ID (see comment)\n");
            //when storage of all students is figured out, deletion will go here using tempID
          }
          else
          { 
            try
            {
              throw new IdException();
            }
            catch (IdException e)
            {
              System.out.println(e.excMsg());
            }

            break;
          }
          
          break;
          
        case 'C': ////////////////////////////////INVOICE FUNCTION////////////////////////////////////////////
          
          System.out.print("Enter Student’s ID: ");
          tempID = stringScan.nextLine().toUpperCase(); //toUpper to make search easier

          if (checkID(tempID) == true) //checks if id is in correct format
          {
        	  for(Student student : studentList) {//goes through all students to see if they have matching id 
        		  if(student.getId().compareToIgnoreCase(tempID) == 0) {
            		  student.printInvoice(); //calls print invoice and then ends 
            		  return;
            	  }
              }
              System.out.println("No Student has that ID");
        	  
        	  
        	  
  
          }
          else
          { 
            try
            {
              throw new IdException();
            }
            catch (IdException e)
            {
              System.out.println(e.excMsg());
            }

            break;
          }
          break;
          
        case 'D': ////////////////////////////////PRINT ALL FUNCTION////////////////////////////////////////////
          System.out.println("All students will be printed here\n");
          ArrayList <Student> undergradStudents= new ArrayList <>();
    	  ArrayList <Student> msStudents= new ArrayList <>();
    	  ArrayList <Student> phdStudents= new ArrayList <>();
          
          for(Student student : studentList) {
        	  
        	  if(student instanceof UndergraduateStudent) {
        		  undergradStudents.add(student);
        	  }
        	  else if(student instanceof MsStudent) {
        		  msStudents.add(student);
        	  }
        	  else if(student instanceof PhdStudent){
        		  phdStudents.add(student);
        	  }
        	  
          }
          System.out.println("PhD Students");
    	  System.out.println("------------");
    	  for(Student student: phdStudents) {
    		  System.out.println("	- " + student.getName());
    	  }
    	  System.out.println("");
    	  
    	  System.out.println("MS Students");
    	  System.out.println("------------");
    	  for(Student student: msStudents) {
    		  System.out.println("	- " + student.getName());
    	  }
    	  System.out.println("");
          
    	  System.out.println("Undergraduate Students");
    	  System.out.println("------------");
    	  for(Student student: undergradStudents) {
    		  System.out.println("	- " + student.getName());
    	  }
    	  System.out.println("");
          break;
          
        case 'X':
          System.out.println("Back to main menu\n");
          break;
          
        default:
          System.out.println("Invalid choice. Please try again.\n");
      }		
    } 
    while (studentChoice != 'X');	
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

  //student management menu methods, if needs to be moved elsewhere lmk ////////////////

  public static boolean checkID(String id) //returns true if id is valid, false if not
  {
    if(id.length() != 6) 
    {
      return false;
    }

    if(Character.isLetter(id.charAt(0)) == false || Character.isLetter(id.charAt(1)) == false)
    {
      return false;
    }

    if(Character.isDigit(id.charAt(2)) == false || Character.isDigit(id.charAt(3)) == false  || Character.isDigit(id.charAt(4)) == false || Character.isDigit(id.charAt(5)) == false)
    {
      return false;
    }

    //NEEDS TO CHECK ALL EXISTING IDS FOR DUPLICATES BEFORE RETURNING TRUE!!!!!!!!!!!!!!

    return true;
  }

  /*
  public static boolean checkCourses(String courses) //returns true if courses are valid, false if not
  {
    String[] checkCourseList = courses.split(",");
    
    for(String tempCourse : checkCourseList) 
    {
      if(tempCourse.length() != 7) 
      {
        return false;
      }

      if(getCourse(tempCourse) == null)
      {
        return false;
      }
      else
      {
        System.out.println(tempCourse + "COURSE WAS FOUND");
      }

      //if 
    
      
      
    }

    return true;
  }
  */
}
//END OF DRIVER CLASS HERE//////

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
      if((course.getCrn() == crn) && (course.getEnabled() == true)) {
    	if(!course.Empty()) {
    		System.out.println("Course is not empty so it cant be deleted");
    	}
    	  
    	  
        //disables course if it doesnt have labs
        if(!course.hasLabs()) {
          course.setEnabled(false);
          System.out.printf("[ %d,%s,%s ] deleted!\n",course.getCrn(), course.getPrefix(), course.getTitle());
          return;
        }
        //disables course if it has labs and there all empty
        if(course.allLabsEmpty()) {
          course.setEnabled(false);
          course.allLabsSetEnabled(false);
          System.out.printf("[ %d,%s,%s ] deleted!\n",course.getCrn(), course.getPrefix(), course.getTitle());
          return;
        }
        System.out.println("Course or its labs are not empty so it cant be deleted");
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
    System.out.printf("Course could not be found: invalid crn\n");
  }

  //gets course if its exists otherwise returns null
  public Course getCourse(int crn) {
    int len = list.size();
    for(int i = 0; i < len; i++) {
      if((list.get(i).getCrn() == crn) && (list.get(i).getEnabled())) {
        return list.get(i);
      }
    }
    return null; //returns null as crn wasn't found

  }

  //gets course if its exists otherwise returns null
  public Course getCourse(String preFix) {
    int len = list.size();
    for(int i = 0; i < len; i++) {
      if(((list.get(i).getPrefix()).compareToIgnoreCase(preFix) == 0) && (list.get(i).getEnabled())) {
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
      hasLab = "YES";
      labList = new ArrayList <Lab>();
      labList.add(lab);
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
  public boolean isGraduate() {
	  if(classLevel.compareToIgnoreCase("graduate") == 0) {
		  return true;
	  }
	  else {
		  return false;
	  }
  }
  public boolean isUndergraduate() {
	  if(classLevel.compareToIgnoreCase("undergraduate") == 0) {
		  return true;
	  }
	  else {
		  return false;
	  }
  }
  public void addPerson() {
	  numPeopleTakingCourse++;
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
  private String id;

  public Student (String name, String id)
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
  public String getId()
  {
    return this.id;
  }
}

class UndergraduateStudent extends Student
{
  private String[] courses; //string array might be better since multiple courses?
  int Resident; //0 means not resident, 1 means yes resident
  public UndergraduateStudent (String name, String id, String[] courses, int isResident)
  {
    super (name, id);
    this.courses = courses;
    Resident = isResident;
  }
double creditHours = 120.25; //base resident credit hour
  public void printInvoice()
  {
    double totalCost = 0;
    System.out.println("VALENCE COLLEGE");
    System.out.println("ORLANDO FL 10101");
    System.out.println("--------------------------\n");
    System.out.println("Fee Invoice Prepared for Student: \n" + getId() + "-" + getName());
    if (Resident == 0)
    {
    	System.out.println("1 Credit Hours = $240.50\n");
    	creditHours = 240.50;
    }
    else
    {
    	System.out.println("1 Credit Hours = $120.25\n");
    }
    System.out.println("CRN        CR_ PREFIX    CR_HOURS");
    //add logic for each course here
    System.out.println("Health & id frees $ 35.00\n");
    System.out.println("--------------------------\n");
    System.out.println("$ " + totalCost);
    //add logic for discount above 500$ payment
    System.out.println("-$ " + (totalCost * 0.25));
    System.out.println("TOTAL PAYMENTS    $ " + (totalCost - (totalCost*0.25)));
  }
}

abstract class GraduateStudent extends Student
{
  public GraduateStudent(String name, String id)
  {
    super(name, id);
  }
}

class MsStudent extends GraduateStudent
{
  private String[] courses; //string array might be better since multiple courses?

  public MsStudent(String name, String id, String[] courses)
  {
    super (name, id);
    this.courses = courses;
  }

  public void printInvoice()
  {
    double totalCost = 0;
    totalCost += 35; //adds health and id fees
    System.out.println("VALENCE COLLEGE");
    System.out.println("ORLANDO FL 10101");
    System.out.println("--------------------------\n");
    System.out.println("Fee Invoice Prepared for Student: \n" + getId() + "-" + getName());
    System.out.println("1 Credit Hours = $300.00\n");
    System.out.println("CRN        CR_ PREFIX    CR_HOURS");
    //add logic for each course here
    System.out.println("\tHealth & id frees $ 35.00\n");
    System.out.println("--------------------------\n");
    System.out.println("TOTAL PAYMENTS    $ " + totalCost);
  }
}

class PhdStudent extends GraduateStudent
{
  private String advisor;
  private String subject;
  private ArrayList<Integer> labCrns; //changed from int array to arraylist 

  public PhdStudent(String name, String id, String advisor, String subject, ArrayList<Integer> labCrns)
  {
    super(name, id);
    this.advisor = advisor;
    this.subject = subject;
    this.labCrns = labCrns;
  }

  public void printInvoice()
  {
    double totalCost = 0;
    totalCost += 735; //adds research fee and health fee
    int numEntries = labCrns.size(); //gets amount of labs the phd student supervises
    if (numEntries >= 3) //logic for lab supervision discounts
    {
    	totalCost = 35; //sets cost equal to only the health and id fees
    }
    else if (numEntries == 2)
    {
    	totalCost = totalCost * .5;
    }
    System.out.println("VALENCE COLLEGE");
    System.out.println("ORLANDO FL 10101");
    System.out.println("--------------------------\n");
    System.out.println("Fee Invoice Prepared for Student: \n" + getId() + "-" + getName());
    System.out.println("1 Credit Hours = $120.25\n");
    System.out.println("\n\nRESEARCH\n");
    if(numEntries >= 3) //prints minus 700 if the PHD student supervises 3 or more labs
    {
    	System.out.println(this.subject + "t$ -700.00\n");
    }
    else
    {
    	System.out.println(this.subject + "t$ 700.00\n");
    }
    System.out.println("\tHealth & id frees $ 35.00\n\n");
    System.out.println("--------------------------\n");
    //logic to remove costs based on entries in array list
    System.out.println("TOTAL PAYMENTS    $ " + (totalCost));
   
  }
}

//exception class here

class IdException extends Exception implements Serializable 
{
  private static final long serialVersionUID = 1L;
  public String excMsg()
  {
    return "\nInvalid ID format or ID already exists";
    //to be thrown in a try block after checking that an ID is wrong format or is a duplicate
  }
}