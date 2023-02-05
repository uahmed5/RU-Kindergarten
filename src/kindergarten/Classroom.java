package kindergarten;

import org.w3c.dom.Node;

/**
 * This class represents a Classroom, with:
 * - an SNode instance variable for students in line,
 * - an SNode instance variable for musical chairs, pointing to the last student in the list,
 * - a boolean array for seating availability (eg. can a student sit in a given seat), and
 * - a Student array parallel to seatingAvailability to show students filed into seats 
 * --- (more formally, seatingAvailability[i][j] also refers to the same seat in studentsSitting[i][j])
 * 
 * @author Ethan Chou
 * @author Kal Pandit
 * @author Maksims Kurjanovics Kravcenko
 */
public class Classroom {
    private SNode studentsInLine;             // when students are in line: references the FIRST student in the LL
    private SNode musicalChairs;              // when students are in musical chairs: references the LAST student in the CLL
    private boolean[][] seatingAvailability;  // represents the classroom seats that are available to students
    private Student[][] studentsSitting;      // when students are sitting in the classroom: contains the students

    /**
     * Constructor for classrooms. Do not edit.
     * @param l passes in students in line
     * @param m passes in musical chairs
     * @param a passes in availability
     * @param s passes in students sitting
     */
    public Classroom ( SNode l, SNode m, boolean[][] a, Student[][] s ) {
		studentsInLine      = l;
        musicalChairs       = m;
		seatingAvailability = a;
        studentsSitting     = s;
	}
    /**
     * Default constructor starts an empty classroom. Do not edit.
     */
    public Classroom() {
        this(null, null, null, null);
    }

    /**
     * This method simulates students coming into the classroom and standing in line.
     * 
     * Reads students from input file and inserts these students in alphabetical 
     * order to studentsInLine singly linked list.
     * 
     * Input file has:
     * 1) one line containing an integer representing the number of students in the file, say x
     * 2) x lines containing one student per line. Each line has the following student 
     * information separated by spaces: FirstName LastName Height
     * 
     * @param filename the student information input file
     */
    public void makeClassroom ( String filename ) {

        // WRITE YOUR CODE HERE
        StdIn.setFile(filename);
        int numOfStudents = StdIn.readInt();
        SNode head = null;
        for(int i = 0; i <  numOfStudents; i++)
        {
            Student student = new Student();
            String firstName = StdIn.readString();
            String lastName = StdIn.readString();
            int height = StdIn.readInt();
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setHeight(height);
            SNode s = new SNode();
            s.setStudent(student);
            if (head == null || head.getStudent().compareNameTo(student) >= 0)
            {
                s.setNext(head);
                head = s;
            }
            else 
            {
                SNode current = head;
                while(current.getNext() != null && current.getNext().getStudent().compareNameTo(student) < 0)
                {
                    current = current.getNext();
                }
                s.setNext(current.getNext()); 
                current.setNext(s);

            }
        }
        studentsInLine = head;
    }

    /**
     * 
     * This method creates and initializes the seatingAvailability (2D array) of 
     * available seats inside the classroom. Imagine that unavailable seats are broken and cannot be used.
     * 
     * Reads seating chart input file with the format:
     * An integer representing the number of rows in the classroom, say r
     * An integer representing the number of columns in the classroom, say c
     * Number of r lines, each containing c true or false values (true denotes an available seat)
     *  
     * This method also creates the studentsSitting array with the same number of
     * rows and columns as the seatingAvailability array
     * 
     * This method does not seat students on the seats.
     * 
     * @param seatingChart the seating chart input file
     */
    public void setupSeats(String seatingChart) {

	    // WRITE YOUR CODE HERE
        StdIn.setFile(seatingChart);
        int rows = StdIn.readInt();
        int columns = StdIn.readInt();
        seatingAvailability = new boolean[rows][columns];
        studentsSitting = new Student[rows][columns];

        for(int r = 0; r < rows; r++)
        {
            for(int c = 0; c < columns; c++)
            {
                seatingAvailability[r][c] = StdIn.readBoolean();
            }
        }

    }

    /**
     * 
     * This method simulates students taking their seats in the classroom.
     * 
     * 1. seats any remaining students from the musicalChairs starting from the front of the list
     * 2. starting from the front of the studentsInLine singly linked list
     * 3. removes one student at a time from the list and inserts them into studentsSitting according to
     *    seatingAvailability
     * 
     * studentsInLine will then be empty
     */
    public void seatStudents () {

	    // WRITE YOUR CODE HERE
        int rows = seatingAvailability.length;
        int columns = seatingAvailability[0].length;

        for(int r = 0; r < rows; r++)
        {
            for(int c = 0; c < columns; c++)
            {
                if(seatingAvailability[r][c] == true)
                {
                    if(musicalChairs != null)
                    {
                        seatingAvailability[r][c] = false;
                        studentsSitting[r][c] = musicalChairs.getStudent();
                        musicalChairs = musicalChairs.getNext();
                        musicalChairs = null;
                    }
                    else if (studentsInLine != null)
                    {
                        seatingAvailability[r][c] = false;
                        studentsSitting[r][c] = studentsInLine.getStudent();
                        studentsInLine = studentsInLine.getNext();
                    }
                }

            }
        }
	
    }

    /**
     * Traverses studentsSitting row-wise (starting at row 0) removing a seated
     * student and adding that student to the end of the musicalChairs list.
     * 
     * row-wise: starts at index [0][0] traverses the entire first row and then moves
     * into second row.
     */
    public void insertMusicalChairs () {
        
        // WRITE YOUR CODE HERE
        for(int r = 0; r < studentsSitting.length; r++)
        {
            for(int c = 0; c < studentsSitting[r].length; c++)
            {
                if (studentsSitting[r][c] != null)
                {
                    SNode sNode = new SNode();
                    sNode.setStudent(studentsSitting[r][c]);
                    if(musicalChairs == null)
                    {
                        sNode.setNext(sNode);
                        musicalChairs = sNode;
                    }
                    else
                    {
                        sNode.setNext(musicalChairs.getNext());
                        musicalChairs.setNext(sNode);
                        musicalChairs = sNode;
                    }
                    studentsSitting[r][c] = null;
                    seatingAvailability[r][c] = true;
                }
            }
        }
     }

    /**
     * 
     * This method repeatedly removes students from the musicalChairs until there is only one
     * student (the winner).
     * 
     * Choose a student to be elimnated from the musicalChairs using StdRandom.uniform(int b),
     * where b is the number of students in the musicalChairs. 0 is the first student in the 
     * list, b-1 is the last.
     * 
     * Removes eliminated student from the list and inserts students back in studentsInLine 
     * in ascending height order (shortest to tallest).
     * 
     * The last line of this method calls the seatStudents() method so that students can be seated.
     */
    public void playMusicalChairs() {

        // WRITE YOUR CODE HERE
        int numOfStudents = 1;
        SNode c = musicalChairs;
        while(c.getNext() != musicalChairs)
        {
            numOfStudents++;
            c = c.getNext();
        }
        for(int ns = 0; ns < numOfStudents - 1; ns++)
        {
            int remainingStudents = numOfStudents - ns;
            int position = StdRandom.uniform(remainingStudents);
            SNode snode = musicalChairs.getNext();

            if (position == 0) snode = musicalChairs;
            for(int i = 0; i < position-1; i++)
            {
                snode = snode.getNext();
            }
            SNode selectedStudentNode = snode.getNext();
            snode.setNext(selectedStudentNode.getNext());

            Student student = selectedStudentNode.getStudent();
            if (selectedStudentNode == musicalChairs)
            {
                musicalChairs = snode;
            }

            if (studentsInLine != null)
            {
                SNode head = studentsInLine;
                if ((head.getStudent().getHeight() - student.getHeight()) >= 0  )
                {
                    selectedStudentNode.setNext(head);
                    studentsInLine = selectedStudentNode;
                }
                else 
                {
                    SNode current = head;
                    while(current.getNext() != null && (current.getNext().getStudent().getHeight() - student.getHeight() < 0))
                    {
                        current = current.getNext();
                    }
                    selectedStudentNode.setNext(current.getNext()); 
                    current.setNext(selectedStudentNode);
                }
            }
            else
            {
                studentsInLine = selectedStudentNode;
                selectedStudentNode.setNext(null);
            }
        }

        seatStudents();
    } 

    /**
     * Insert a student to wherever the students are at (ie. whatever activity is not empty)
     * Note: adds to the end of either linked list or the next available empty seat
     * @param firstName the first name
     * @param lastName the last name
     * @param height the height of the student
     */
    public void addLateStudent ( String firstName, String lastName, int height ) {
        
        // WRITE YOUR CODE HERE
        SNode lsNode = new SNode();
        Student lateStudent = new Student(firstName, lastName, height);
        lsNode.setStudent(lateStudent);
        if(musicalChairs != null)
        {
            lsNode.setNext(musicalChairs.getNext());
            musicalChairs.setNext(lsNode);
            musicalChairs = lsNode;
        }
        if(studentsInLine != null)
        {
            SNode current = studentsInLine;
            while(current.getNext() != null)
            {
                current = current.getNext();
            }
            current.setNext(lsNode);
            lsNode.setNext(null);

        }
        if(musicalChairs == null && studentsInLine == null)
        {
            int rows = seatingAvailability.length;
            int columns = seatingAvailability[0].length;
            for(int r = 0; r < rows; r++)
            {
                for(int c = 0; c < columns; c++)
                {
                    if(lateStudent != null && seatingAvailability[r][c] == true)
                    {
                        seatingAvailability[r][c] = false;
                        studentsSitting[r][c] = lateStudent;
                        lateStudent = null;
                    }
                }
            }
        }
    }

    /**
     * A student decides to leave early
     * This method deletes an early-leaving student from wherever the students 
     * are at (ie. whatever activity is not empty)
     * 
     * Assume the student's name is unique
     * 
     * @param firstName the student's first name
     * @param lastName the student's last name
     */
    public void deleteLeavingStudent ( String firstName, String lastName ) {

        // WRITE YOUR CODE HERE
        if(musicalChairs != null)
        {
            SNode remove = musicalChairs;
            SNode previous = null;
            Student student = musicalChairs.getStudent();
            if(firstName.equalsIgnoreCase(student.getFirstName()) && lastName.equalsIgnoreCase(student.getLastName()) ) {
                if (musicalChairs.getNext() == musicalChairs) {
                    musicalChairs = null;
                }
                else {
                    SNode current = musicalChairs.getNext();
                    while (current.getNext() != musicalChairs)
                    {
                        current = current.getNext();
                    }
                    current.setNext(musicalChairs.getNext());
                    musicalChairs = current;
                }
            }
            else    
            {
                while(remove.getNext() != musicalChairs)
                {
                    Student removedStudent = remove.getStudent();

                    if(firstName.equalsIgnoreCase(removedStudent.getFirstName()) && lastName.equalsIgnoreCase(removedStudent.getLastName()) )
                    {
                        previous.setNext(remove.getNext());
                        break;
                    }
                    previous = remove;
                    remove = remove.getNext();
                }
            }
        }
        else if(studentsInLine != null)
        {
            SNode previous = null;

            SNode current = studentsInLine;
            while(current != null)
            {
                Student removedStudent = current.getStudent();

                if(firstName.equalsIgnoreCase(removedStudent.getFirstName()) && lastName.equalsIgnoreCase(removedStudent.getLastName()) )
                {
                    if(previous != null)
                    {
                        previous.setNext(current.getNext());
                    }
                    else
                    {
                        studentsInLine = current.getNext();
                    }
                    break;
                }
                previous = current;
                current = current.getNext();
            }

        }
        else if(musicalChairs == null && studentsInLine == null)
        {
            int rows = seatingAvailability.length;
            int columns = seatingAvailability[0].length;
            for(int r = 0; r < rows; r++)
            {
                for(int c = 0; c < columns; c++)
                {
                    Student student = studentsSitting[r][c];
                    if(student != null)
                    {
                        if(firstName.equalsIgnoreCase(student.getFirstName()) && lastName.equalsIgnoreCase(student.getLastName()) )
                        {
                            studentsSitting[r][c] = null;
                            seatingAvailability[r][c] = true;
                        }
                }
                }
            }
        }

    }

    /**
     * Used by driver to display students in line
     * DO NOT edit.
     */
    public void printStudentsInLine () {

        //Print studentsInLine
        StdOut.println ( "Students in Line:" );
        if ( studentsInLine == null ) { StdOut.println("EMPTY"); }

        for ( SNode ptr = studentsInLine; ptr != null; ptr = ptr.getNext() ) {
            StdOut.print ( ptr.getStudent().print() );
            if ( ptr.getNext() != null ) { StdOut.print ( " -> " ); }
        }
        StdOut.println();
        StdOut.println();
    }

    /**
     * Prints the seated students; can use this method to debug.
     * DO NOT edit.
     */
    public void printSeatedStudents () {

        StdOut.println("Sitting Students:");

        if ( studentsSitting != null ) {
        
            for ( int i = 0; i < studentsSitting.length; i++ ) {
                for ( int j = 0; j < studentsSitting[i].length; j++ ) {

                    String stringToPrint = "";
                    if ( studentsSitting[i][j] == null ) {

                        if (seatingAvailability[i][j] == false) {stringToPrint = "X";}
                        else { stringToPrint = "EMPTY"; }

                    } else { stringToPrint = studentsSitting[i][j].print();}

                    StdOut.print ( stringToPrint );
                    
                    for ( int o = 0; o < (10 - stringToPrint.length()); o++ ) {
                        StdOut.print (" ");
                    }
                }
                StdOut.println();
            }
        } else {
            StdOut.println("EMPTY");
        }
        StdOut.println();
    }

    /**
     * Prints the musical chairs; can use this method to debug.
     * DO NOT edit.
     */
    public void printMusicalChairs () {
        StdOut.println ( "Students in Musical Chairs:" );

        if ( musicalChairs == null ) {
            StdOut.println("EMPTY");
            StdOut.println();
            return;
        }
        SNode ptr;
        for ( ptr = musicalChairs.getNext(); ptr != musicalChairs; ptr = ptr.getNext() ) {
            StdOut.print(ptr.getStudent().print() + " -> ");
        }
        if ( ptr == musicalChairs) {
            StdOut.print(musicalChairs.getStudent().print() + " - POINTS TO FRONT");
        }
        StdOut.println();
    }

    /**
     * Prints the state of the classroom; can use this method to debug.
     * DO NOT edit.
     */
    public void printClassroom() {
        printStudentsInLine();
        printSeatedStudents();
        printMusicalChairs();
    }

    /**
     * Used to get and set objects.
     * DO NOT edit.
     */

    public SNode getStudentsInLine() { return studentsInLine; }
    public void setStudentsInLine(SNode l) { studentsInLine = l; }

    public SNode getMusicalChairs() { return musicalChairs; }
    public void setMusicalChairs(SNode m) { musicalChairs = m; }

    public boolean[][] getSeatingAvailability() { return seatingAvailability; }
    public void setSeatingAvailability(boolean[][] a) { seatingAvailability = a; }

    public Student[][] getStudentsSitting() { return studentsSitting; }
    public void setStudentsSitting(Student[][] s) { studentsSitting = s; }

}
