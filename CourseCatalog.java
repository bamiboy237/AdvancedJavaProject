/*
 * Guy-robert Bogning
 * Course catalog with sample courses for the LMS.
 */

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class CourseCatalog implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<Course> courses;

    public CourseCatalog() {
        courses = new ArrayList<>();
        initializeCourses();
    }

    private void initializeCourses() {
        courses.add(
            createCourse(
                "CSC-101",
                "Introduction to Computer Science",
                "Foundations of programming and computational thinking.",
                new String[] {
                    "Week 1: Introduction to CS",
                    "Week 2: Algorithms",
                    "Week 3: Variables",
                    "Week 4: Control Flow",
                    "Week 5: Functions",
                },
                new String[] {
                    "Lab 1: Hello World",
                    "Quiz 1: Basics",
                    "Midterm Exam",
                    "Lab 2: Functions",
                    "Final Project",
                },
                "Dr. Smith",
                0
            )
        );

        courses.add(
            createCourse(
                "CSC-102",
                "Data Structures",
                "Arrays, linked lists, stacks, queues, trees, and graphs.",
                new String[] {
                    "Week 1: Arrays",
                    "Week 2: Linked Lists",
                    "Week 3: Stacks & Queues",
                    "Week 4: Trees",
                    "Week 5: Graphs",
                },
                new String[] {
                    "Lab 1: Linked List Implementation",
                    "Quiz 1: Arrays",
                    "Midterm Exam",
                    "Lab 2: Binary Tree",
                    "Final Project",
                },
                "Dr. Johnson",
                1
            )
        );

        courses.add(
            createCourse(
                "CSC-201",
                "Object-Oriented Programming",
                "OOP principles: encapsulation, inheritance, polymorphism.",
                new String[] {
                    "Week 1: Classes & Objects",
                    "Week 2: Inheritance",
                    "Week 3: Polymorphism",
                    "Week 4: Interfaces",
                    "Week 5: Design Patterns",
                },
                new String[] {
                    "Lab 1: Class Design",
                    "Quiz 1: OOP Concepts",
                    "Midterm Project",
                    "Lab 2: Inheritance",
                    "Final Project",
                },
                "Dr. Williams",
                2
            )
        );

        courses.add(
            createCourse(
                "CSC-202",
                "Algorithms",
                "Algorithm analysis, sorting, searching, and complexity.",
                new String[] {
                    "Week 1: Big-O Notation",
                    "Week 2: Sorting Algorithms",
                    "Week 3: Searching",
                    "Week 4: Divide & Conquer",
                    "Week 5: Dynamic Programming",
                },
                new String[] {
                    "Lab 1: Sorting Analysis",
                    "Quiz 1: Complexity",
                    "Midterm Exam",
                    "Lab 2: Dynamic Programming",
                    "Final Exam",
                },
                "Dr. Brown",
                3
            )
        );

        courses.add(
            createCourse(
                "MTH-101",
                "Discrete Mathematics",
                "Logic, sets, relations, functions, and combinatorics.",
                new String[] {
                    "Week 1: Propositional Logic",
                    "Week 2: Sets",
                    "Week 3: Relations",
                    "Week 4: Functions",
                    "Week 5: Counting",
                },
                new String[] {
                    "Problem Set 1",
                    "Quiz 1: Logic",
                    "Midterm Exam",
                    "Problem Set 2",
                    "Final Exam",
                },
                "Dr. Davis",
                4
            )
        );

        courses.add(
            createCourse(
                "MTH-201",
                "Linear Algebra",
                "Vectors, matrices, and linear transformations.",
                new String[] {
                    "Week 1: Vectors",
                    "Week 2: Matrices",
                    "Week 3: Matrix Operations",
                    "Week 4: Linear Transformations",
                    "Week 5: Eigenvalues",
                },
                new String[] {
                    "Homework 1",
                    "Quiz 1: Vectors",
                    "Midterm Exam",
                    "Homework 2",
                    "Final Exam",
                },
                "Dr. Miller",
                5
            )
        );

        courses.add(
            createCourse(
                "CSC-301",
                "Database Systems",
                "Relational databases, SQL, and database design.",
                new String[] {
                    "Week 1: ER Diagrams",
                    "Week 2: Relational Model",
                    "Week 3: SQL Basics",
                    "Week 4: Advanced SQL",
                    "Week 5: Normalization",
                },
                new String[] {
                    "Lab 1: ER Design",
                    "Quiz 1: SQL",
                    "Midterm Project",
                    "Lab 2: Database Design",
                    "Final Project",
                },
                "Dr. Wilson",
                6
            )
        );

        courses.add(
            createCourse(
                "CSC-302",
                "Operating Systems",
                "Process management, memory, file systems, and concurrency.",
                new String[] {
                    "Week 1: Process Management",
                    "Week 2: Threads",
                    "Week 3: Memory Management",
                    "Week 4: File Systems",
                    "Week 5: Concurrency",
                },
                new String[] {
                    "Lab 1: Process Scheduling",
                    "Quiz 1: Threads",
                    "Midterm Exam",
                    "Lab 2: Memory Allocation",
                    "Final Project",
                },
                "Dr. Moore",
                7
            )
        );

        courses.add(
            createCourse(
                "CSC-303",
                "Computer Networks",
                "Network layers, protocols, and distributed systems.",
                new String[] {
                    "Week 1: OSI Model",
                    "Week 2: TCP/IP",
                    "Week 3: Routing",
                    "Week 4: Application Layer",
                    "Week 5: Security",
                },
                new String[] {
                    "Lab 1: Wireshark",
                    "Quiz 1: Protocols",
                    "Midterm Exam",
                    "Lab 2: Socket Programming",
                    "Final Project",
                },
                "Dr. Taylor",
                8
            )
        );

        courses.add(
            createCourse(
                "CSC-304",
                "Software Engineering",
                "SDLC, agile, testing, and project management.",
                new String[] {
                    "Week 1: SDLC Models",
                    "Week 2: Requirements",
                    "Week 3: Design",
                    "Week 4: Testing",
                    "Week 5: Agile",
                },
                new String[] {
                    "Lab 1: Requirements Doc",
                    "Quiz 1: SDLC",
                    "Midterm Project",
                    "Lab 2: Unit Testing",
                    "Final Project",
                },
                "Dr. Anderson",
                9
            )
        );

        courses.add(
            createCourse(
                "CSC-305",
                "Web Development",
                "HTML, CSS, JavaScript, and web frameworks.",
                new String[] {
                    "Week 1: HTML Basics",
                    "Week 2: CSS Styling",
                    "Week 3: JavaScript",
                    "Week 4: DOM Manipulation",
                    "Week 5: Frameworks",
                },
                new String[] {
                    "Lab 1: Portfolio Site",
                    "Quiz 1: HTML/CSS",
                    "Midterm Project",
                    "Lab 2: Interactive App",
                    "Final Project",
                },
                "Dr. Thomas",
                10
            )
        );

        courses.add(
            createCourse(
                "CSC-306",
                "Mobile App Development",
                "Android/iOS development with modern frameworks.",
                new String[] {
                    "Week 1: Mobile Fundamentals",
                    "Week 2: UI Design",
                    "Week 3: Data Storage",
                    "Week 4: APIs",
                    "Week 5: Deployment",
                },
                new String[] {
                    "Lab 1: Hello App",
                    "Quiz 1: UI Design",
                    "Midterm Project",
                    "Lab 2: REST Integration",
                    "Final Project",
                },
                "Dr. Jackson",
                11
            )
        );

        courses.add(
            createCourse(
                "CSC-307",
                "Artificial Intelligence",
                "Machine learning, neural networks, and AI ethics.",
                new String[] {
                    "Week 1: AI Fundamentals",
                    "Week 2: Search Algorithms",
                    "Week 3: Machine Learning",
                    "Week 4: Neural Networks",
                    "Week 5: Ethics",
                },
                new String[] {
                    "Lab 1: Search Implementation",
                    "Quiz 1: ML Basics",
                    "Midterm Exam",
                    "Lab 2: Neural Network",
                    "Final Project",
                },
                "Dr. White",
                12
            )
        );

        courses.add(
            createCourse(
                "CSC-308",
                "Cybersecurity",
                "Network security, cryptography, and risk management.",
                new String[] {
                    "Week 1: Security Basics",
                    "Week 2: Cryptography",
                    "Week 3: Network Security",
                    "Week 4: Authentication",
                    "Week 5: Incident Response",
                },
                new String[] {
                    "Lab 1: Password Security",
                    "Quiz 1: Cryptography",
                    "Midterm Exam",
                    "Lab 2: Network Scanning",
                    "Final Project",
                },
                "Dr. Harris",
                13
            )
        );

        courses.add(
            createCourse(
                "CSC-309",
                "Cloud Computing",
                "Distributed systems, virtualization, and cloud services.",
                new String[] {
                    "Week 1: Cloud Models",
                    "Week 2: Virtualization",
                    "Week 3: Containers",
                    "Week 4: Cloud Services",
                    "Week 5: DevOps",
                },
                new String[] {
                    "Lab 1: AWS Setup",
                    "Quiz 1: IaaS/PaaS/SaaS",
                    "Midterm Project",
                    "Lab 2: Docker",
                    "Final Project",
                },
                "Dr. Martin",
                14
            )
        );

        courses.add(
            createCourse(
                "CSC-310",
                "Computer Graphics",
                "2D/3D graphics, rendering, and shader programming.",
                new String[] {
                    "Week 1: Graphics Pipeline",
                    "Week 2: Transformations",
                    "Week 3: Rendering",
                    "Week 4: Shaders",
                    "Week 5: Animation",
                },
                new String[] {
                    "Lab 1: 2D Shapes",
                    "Quiz 1: Transformations",
                    "Midterm Project",
                    "Lab 2: 3D Scene",
                    "Final Project",
                },
                "Dr. Thompson",
                15
            )
        );

        courses.add(
            createCourse(
                "CSC-311",
                "Compiler Design",
                "Lexical analysis, parsing, and code generation.",
                new String[] {
                    "Week 1: Lexical Analysis",
                    "Week 2: Parsing",
                    "Week 3: Syntax Trees",
                    "Week 4: Semantic Analysis",
                    "Week 5: Code Generation",
                },
                new String[] {
                    "Lab 1: Lexer",
                    "Quiz 1: Parsing",
                    "Midterm Exam",
                    "Lab 2: Parser",
                    "Final Project",
                },
                "Dr. Garcia",
                16
            )
        );

        courses.add(
            createCourse(
                "CSC-312",
                "Game Development",
                "Game engines, physics, and interactive design.",
                new String[] {
                    "Week 1: Game Design",
                    "Week 2: Game Engines",
                    "Week 3: Physics",
                    "Week 4: Audio",
                    "Week 5: Optimization",
                },
                new String[] {
                    "Lab 1: Pong Clone",
                    "Quiz 1: Game Loop",
                    "Midterm Project",
                    "Lab 2: Platformer",
                    "Final Game",
                },
                "Dr. Martinez",
                17
            )
        );

        courses.add(
            createCourse(
                "PHY-101",
                "Physics for Computer Scientists",
                "Mechanics, electricity, and computing applications.",
                new String[] {
                    "Week 1: Kinematics",
                    "Week 2: Forces",
                    "Week 3: Energy",
                    "Week 4: Electricity",
                    "Week 5: Circuits",
                },
                new String[] {
                    "Lab 1: Motion",
                    "Quiz 1: Mechanics",
                    "Midterm Exam",
                    "Lab 2: Circuits",
                    "Final Exam",
                },
                "Dr. Robinson",
                18
            )
        );

        courses.add(
            createCourse(
                "ENG-101",
                "Technical Writing",
                "Communication skills for engineers and developers.",
                new String[] {
                    "Week 1: Documentation",
                    "Week 2: Reports",
                    "Week 3: Presentations",
                    "Week 4: GitHub README",
                    "Week 5: Code Comments",
                },
                new String[] {
                    "Assignment 1: Memo",
                    "Quiz 1: Writing",
                    "Midterm: Technical Report",
                    "Assignment 2: Presentation",
                    "Final Project",
                },
                "Dr. Clark",
                19
            )
        );
    }

    private Course createCourse(
        String courseId,
        String title,
        String description,
        String[] topics,
        String[] assignments,
        String instructor,
        int scheduleOffset
    ) {
        return new Course(
            courseId,
            title,
            description,
            topics,
            assignments,
            createDeadlines(courseId, title, assignments, scheduleOffset),
            instructor
        );
    }

    private ArrayList<CalendarEvent> createDeadlines(
        String courseId,
        String courseTitle,
        String[] assignments,
        int scheduleOffset
    ) {
        ArrayList<CalendarEvent> deadlines = new ArrayList<>();
        LocalDateTime baseDate = LocalDateTime.now()
            .withHour(23)
            .withMinute(59)
            .withSecond(0)
            .withNano(0)
            .plusDays(3L + scheduleOffset);

        for (int i = 0; i < assignments.length; i++) {
            String assignment = assignments[i];
            LocalDateTime dueDate = baseDate.plusDays(i * 7L);
            deadlines.add(
                new CalendarEvent(
                    courseId + ": " + assignment,
                    assignment + " for " + courseTitle,
                    "Course Deadline",
                    null,
                    dueDate
                )
            );
        }

        return deadlines;
    }

    public ArrayList<Course> getCourses() {
        return new ArrayList<>(courses);
    }

    public Course getCourseById(String courseId) {
        for (Course course : courses) {
            if (course.getCourseId().equals(courseId)) {
                return course;
            }
        }
        return null;
    }
}
