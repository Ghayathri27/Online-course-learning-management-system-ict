package coursemnt;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/lms")
public class lmsservlet extends HttpServlet {

    // =========================================================
    // GET METHOD
    // =========================================================

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null || action.trim().isEmpty()) {
            action = "dashboard";
        }

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        if (action.equals("dashboard")) {

            showDashboard(out);

        } else if (action.equals("courses")) {

            showCourses(out);

        } else if (action.equals("students")) {

            showStudents(out);

        } else if (action.equals("lessons")) {

            showLessons(out);

        } else if (action.equals("enrollments")) {

            showEnrollments(out);

        } else {

            showDashboard(out);
        }
    }

    // =========================================================
    // POST METHOD
    // =========================================================

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if (action == null || action.trim().isEmpty()) {

            response.sendRedirect("lms?action=dashboard");
            return;
        }

        if (action.equals("addCourse")) {

            addCourse(request, response);

        } else if (action.equals("addStudent")) {

            addStudent(request, response);

        } else if (action.equals("addLesson")) {

            addLesson(request, response);

        } else if (action.equals("enroll")) {

            enrollStudent(request, response);

        } else {

            response.sendRedirect("lms?action=dashboard");
        }
    }

    // =========================================================
    // DASHBOARD
    // =========================================================

    private void showDashboard(PrintWriter out) {

        int courses = getCount("courses");
        int students = getCount("students");
        int lessons = getCount("lessons");
        int enrollments = getCount("enrollments");

        pageStart(out, "Dashboard");

        out.println("<div class='hero'>");

        out.println("<div>");

        out.println("<span class='badge'>LEARN • BUILD • CONQUER</span>");

        out.println("<h1>Code <span>&</span> Conquer</h1>");

        out.println("<p>Your smart learning management platform.</p>");

        out.println(
                "<a class='hero-btn' href='lms?action=courses'>Explore Courses →</a>"
        );

        out.println("</div>");

        out.println("</div>");

        out.println(
                "<h2 class='section-title'>Learning Overview</h2>"
        );

        out.println("<div class='cards'>");

        card(out, "📚", "Courses", courses);
        card(out, "👩‍🎓", "Students", students);
        card(out, "📖", "Lessons", lessons);
        card(out, "🎯", "Enrollments", enrollments);

        out.println("</div>");

        out.println("<div class='quick'>");

        out.println("<h2>Quick Actions</h2>");

        out.println("<div class='quick-grid'>");

        out.println(
                "<a href='lms?action=courses'>📚 Manage Courses</a>"
        );

        out.println(
                "<a href='lms?action=lessons'>📖 Manage Lessons</a>"
        );

        out.println(
                "<a href='lms?action=students'>👩‍🎓 Manage Students</a>"
        );

        out.println(
                "<a href='lms?action=enrollments'>🎯 Manage Enrollments</a>"
        );

        out.println("</div>");

        out.println("</div>");

        pageEnd(out);
    }

    // =========================================================
    // COURSE PAGE
    // =========================================================

    private void showCourses(PrintWriter out) {

        pageStart(out, "Courses");

        out.println("<div class='page-header'>");

        out.println("<h1>📚 Courses</h1>");

        out.println("<p>Manage all available courses.</p>");

        out.println("</div>");

        // FORM

        out.println("<div class='form-box'>");

        out.println("<h2>Add New Course</h2>");

        out.println("<form method='post' action='lms'>");

        out.println(
                "<input type='hidden' name='action' value='addCourse'>"
        );

        out.println(
                "<input type='text' name='course_name' " +
                "placeholder='Course Name' required>"
        );

        out.println(
                "<input type='text' name='description' " +
                "placeholder='Course Description' required>"
        );

        out.println(
                "<input type='text' name='instructor' " +
                "placeholder='Instructor' required>"
        );

        out.println(
                "<input type='text' name='duration' " +
                "placeholder='Duration e.g. 8 Weeks' required>"
        );

        out.println(
                "<button type='submit'>Add Course</button>"
        );

        out.println("</form>");

        out.println("</div>");

        // TABLE

        out.println("<div class='table-box'>");

        out.println("<h2>Available Courses</h2>");

        out.println("<table>");

        out.println("<tr>");

        out.println("<th>ID</th>");
        out.println("<th>Course</th>");
        out.println("<th>Description</th>");
        out.println("<th>Instructor</th>");
        out.println("<th>Duration</th>");

        out.println("</tr>");

        String sql = "SELECT * FROM courses ORDER BY course_id DESC";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                out.println("<tr>");

                out.println(
                        "<td>" +
                        rs.getInt("course_id") +
                        "</td>"
                );

                out.println(
                        "<td><b>" +
                        escapeHtml(rs.getString("course_name")) +
                        "</b></td>"
                );

                out.println(
                        "<td>" +
                        escapeHtml(rs.getString("description")) +
                        "</td>"
                );

                out.println(
                        "<td>" +
                        escapeHtml(rs.getString("instructor")) +
                        "</td>"
                );

                out.println(
                        "<td>" +
                        escapeHtml(rs.getString("duration")) +
                        "</td>"
                );

                out.println("</tr>");
            }

        } catch (Exception e) {

            e.printStackTrace();

            out.println(
                    "<tr><td colspan='5' class='error'>" +
                    "Database Error: " +
                    escapeHtml(e.getMessage()) +
                    "</td></tr>"
            );
        }

        out.println("</table>");

        out.println("</div>");

        pageEnd(out);
    }

    // =========================================================
    // STUDENT PAGE
    // =========================================================

    private void showStudents(PrintWriter out) {

        pageStart(out, "Students");

        out.println("<div class='page-header'>");

        out.println("<h1>👩‍🎓 Students</h1>");

        out.println("<p>Register and manage learners.</p>");

        out.println("</div>");

        // FORM

        out.println("<div class='form-box'>");

        out.println("<h2>Register Student</h2>");

        out.println("<form method='post' action='lms'>");

        out.println(
                "<input type='hidden' name='action' value='addStudent'>"
        );

        out.println(
                "<input type='text' name='name' " +
                "placeholder='Full Name' required>"
        );

        out.println(
                "<input type='email' name='email' " +
                "placeholder='Email Address' required>"
        );

        out.println(
                "<input type='text' name='phone' " +
                "placeholder='Phone Number'>"
        );

        out.println(
                "<button type='submit'>Register Student</button>"
        );

        out.println("</form>");

        out.println("</div>");

        // TABLE

        out.println("<div class='table-box'>");

        out.println("<h2>Registered Students</h2>");

        out.println("<table>");

        out.println("<tr>");

        out.println("<th>ID</th>");
        out.println("<th>Name</th>");
        out.println("<th>Email</th>");
        out.println("<th>Phone</th>");

        out.println("</tr>");

        String sql =
                "SELECT * FROM students ORDER BY student_id DESC";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                out.println("<tr>");

                out.println(
                        "<td>" +
                        rs.getInt("student_id") +
                        "</td>"
                );

                out.println(
                        "<td><b>" +
                        escapeHtml(rs.getString("name")) +
                        "</b></td>"
                );

                out.println(
                        "<td>" +
                        escapeHtml(rs.getString("email")) +
                        "</td>"
                );

                out.println(
                        "<td>" +
                        escapeHtml(rs.getString("phone")) +
                        "</td>"
                );

                out.println("</tr>");
            }

        } catch (Exception e) {

            e.printStackTrace();

            out.println(
                    "<tr><td colspan='4' class='error'>" +
                    "Database Error: " +
                    escapeHtml(e.getMessage()) +
                    "</td></tr>"
            );
        }

        out.println("</table>");

        out.println("</div>");

        pageEnd(out);
    }

    // =========================================================
    // LESSON PAGE
    // =========================================================

    private void showLessons(PrintWriter out) {

        pageStart(out, "Lessons");

        out.println("<div class='page-header'>");

        out.println("<h1>📖 Lessons</h1>");

        out.println("<p>Create lessons for your courses.</p>");

        out.println("</div>");

        // FORM

        out.println("<div class='form-box'>");

        out.println("<h2>Add Lesson</h2>");

        out.println("<form method='post' action='lms'>");

        out.println(
                "<input type='hidden' name='action' value='addLesson'>"
        );

        out.println(
                "<select name='course_id' required>"
        );

        out.println(
                "<option value=''>Select Course</option>"
        );

        String courseSql =
                "SELECT course_id, course_name " +
                "FROM courses ORDER BY course_name";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps =
                        con.prepareStatement(courseSql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                out.println(
                        "<option value='" +
                        rs.getInt("course_id") +
                        "'>" +
                        escapeHtml(rs.getString("course_name")) +
                        "</option>"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            out.println(
                    "<option value=''>Error loading courses</option>"
            );
        }

        out.println("</select>");

        out.println(
                "<input type='text' name='lesson_title' " +
                "placeholder='Lesson Title' required>"
        );

        out.println(
                "<textarea name='lesson_content' " +
                "placeholder='Lesson Content' required></textarea>"
        );

        out.println(
                "<button type='submit'>Add Lesson</button>"
        );

        out.println("</form>");

        out.println("</div>");

        // TABLE

        out.println("<div class='table-box'>");

        out.println("<h2>All Lessons</h2>");

        out.println("<table>");

        out.println("<tr>");

        out.println("<th>ID</th>");
        out.println("<th>Course</th>");
        out.println("<th>Lesson</th>");
        out.println("<th>Content</th>");

        out.println("</tr>");

        String sql =
                "SELECT l.lesson_id, c.course_name, " +
                "l.lesson_title, l.lesson_content " +
                "FROM lessons l " +
                "JOIN courses c ON l.course_id = c.course_id " +
                "ORDER BY l.lesson_id DESC";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                out.println("<tr>");

                out.println(
                        "<td>" +
                        rs.getInt("lesson_id") +
                        "</td>"
                );

                out.println(
                        "<td>" +
                        escapeHtml(rs.getString("course_name")) +
                        "</td>"
                );

                out.println(
                        "<td><b>" +
                        escapeHtml(rs.getString("lesson_title")) +
                        "</b></td>"
                );

                out.println(
                        "<td>" +
                        escapeHtml(rs.getString("lesson_content")) +
                        "</td>"
                );

                out.println("</tr>");
            }

        } catch (Exception e) {

            e.printStackTrace();

            out.println(
                    "<tr><td colspan='4' class='error'>" +
                    "Database Error: " +
                    escapeHtml(e.getMessage()) +
                    "</td></tr>"
            );
        }

        out.println("</table>");

        out.println("</div>");

        pageEnd(out);
    }

    // =========================================================
    // ENROLLMENT PAGE
    // =========================================================

    private void showEnrollments(PrintWriter out) {

        pageStart(out, "Enrollments");

        out.println("<div class='page-header'>");

        out.println("<h1>🎯 Enrollments</h1>");

        out.println("<p>Enroll students into courses.</p>");

        out.println("</div>");

        // FORM

        out.println("<div class='form-box'>");

        out.println("<h2>New Enrollment</h2>");

        out.println("<form method='post' action='lms'>");

        out.println(
                "<input type='hidden' name='action' value='enroll'>"
        );

        // STUDENT DROPDOWN

        out.println(
                "<select name='student_id' required>"
        );

        out.println(
                "<option value=''>Select Student</option>"
        );

        String studentSql =
                "SELECT student_id, name " +
                "FROM students ORDER BY name";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps =
                        con.prepareStatement(studentSql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                out.println(
                        "<option value='" +
                        rs.getInt("student_id") +
                        "'>" +
                        escapeHtml(rs.getString("name")) +
                        "</option>"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            out.println(
                    "<option value=''>Error loading students</option>"
            );
        }

        out.println("</select>");

        // COURSE DROPDOWN

        out.println(
                "<select name='course_id' required>"
        );

        out.println(
                "<option value=''>Select Course</option>"
        );

        String courseSql =
                "SELECT course_id, course_name " +
                "FROM courses ORDER BY course_name";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps =
                        con.prepareStatement(courseSql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                out.println(
                        "<option value='" +
                        rs.getInt("course_id") +
                        "'>" +
                        escapeHtml(rs.getString("course_name")) +
                        "</option>"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            out.println(
                    "<option value=''>Error loading courses</option>"
            );
        }

        out.println("</select>");

        out.println(
                "<button type='submit'>Enroll Student</button>"
        );

        out.println("</form>");

        out.println("</div>");

        // TABLE

        out.println("<div class='table-box'>");

        out.println("<h2>Enrollment Records</h2>");

        out.println("<table>");

        out.println("<tr>");

        out.println("<th>ID</th>");
        out.println("<th>Student</th>");
        out.println("<th>Course</th>");
        out.println("<th>Date</th>");

        out.println("</tr>");

        String sql =
                "SELECT e.enrollment_id, " +
                "s.name, c.course_name, " +
                "e.enrollment_date " +
                "FROM enrollments e " +
                "JOIN students s " +
                "ON e.student_id = s.student_id " +
                "JOIN courses c " +
                "ON e.course_id = c.course_id " +
                "ORDER BY e.enrollment_id DESC";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                out.println("<tr>");

                out.println(
                        "<td>" +
                        rs.getInt("enrollment_id") +
                        "</td>"
                );

                out.println(
                        "<td><b>" +
                        escapeHtml(rs.getString("name")) +
                        "</b></td>"
                );

                out.println(
                        "<td>" +
                        escapeHtml(rs.getString("course_name")) +
                        "</td>"
                );

                out.println(
                        "<td>" +
                        rs.getString("enrollment_date") +
                        "</td>"
                );

                out.println("</tr>");
            }

        } catch (Exception e) {

            e.printStackTrace();

            out.println(
                    "<tr><td colspan='4' class='error'>" +
                    "Database Error: " +
                    escapeHtml(e.getMessage()) +
                    "</td></tr>"
            );
        }

        out.println("</table>");

        out.println("</div>");

        pageEnd(out);
    }

    // =========================================================
    // ADD COURSE
    // =========================================================

    private void addCourse(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String name = request.getParameter("course_name");
        String description = request.getParameter("description");
        String instructor = request.getParameter("instructor");
        String duration = request.getParameter("duration");

        if (name == null || name.trim().isEmpty() ||
            description == null || description.trim().isEmpty() ||
            instructor == null || instructor.trim().isEmpty() ||
            duration == null || duration.trim().isEmpty()) {

            response.getWriter().println(
                    "All course fields are required."
            );

            return;
        }

        String sql =
                "INSERT INTO courses " +
                "(course_name, description, instructor, duration) " +
                "VALUES (?, ?, ?, ?)";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, name.trim());
            ps.setString(2, description.trim());
            ps.setString(3, instructor.trim());
            ps.setString(4, duration.trim());

            int rows = ps.executeUpdate();

            System.out.println(
                    "Course inserted. Rows affected = " + rows
            );

            response.sendRedirect(
                    "lms?action=courses"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType(
                    "text/html;charset=UTF-8"
            );

            PrintWriter out = response.getWriter();

            out.println("<h2>Course Database Error</h2>");

            out.println("<pre>");

            e.printStackTrace(out);

            out.println("</pre>");
        }
    }

    // =========================================================
    // ADD STUDENT
    // =========================================================

    private void addStudent(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        if (name == null || name.trim().isEmpty() ||
            email == null || email.trim().isEmpty()) {

            response.getWriter().println(
                    "Name and email are required."
            );

            return;
        }

        String sql =
                "INSERT INTO students " +
                "(name, email, phone) " +
                "VALUES (?, ?, ?)";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, name.trim());
            ps.setString(2, email.trim());

            if (phone == null || phone.trim().isEmpty()) {
                ps.setNull(
                        3,
                        java.sql.Types.VARCHAR
                );
            } else {
                ps.setString(3, phone.trim());
            }

            int rows = ps.executeUpdate();

            System.out.println(
                    "Student inserted. Rows affected = " + rows
            );

            response.sendRedirect(
                    "lms?action=students"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType(
                    "text/html;charset=UTF-8"
            );

            PrintWriter out = response.getWriter();

            out.println("<h2>Student Database Error</h2>");

            out.println("<pre>");

            e.printStackTrace(out);

            out.println("</pre>");
        }
    }

    // =========================================================
    // ADD LESSON
    // =========================================================

    private void addLesson(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String courseIdText =
                request.getParameter("course_id");

        String title =
                request.getParameter("lesson_title");

        String content =
                request.getParameter("lesson_content");

        if (courseIdText == null ||
            courseIdText.trim().isEmpty()) {

            response.getWriter().println(
                    "Please select a course."
            );

            return;
        }

        if (title == null || title.trim().isEmpty() ||
            content == null || content.trim().isEmpty()) {

            response.getWriter().println(
                    "Lesson title and content are required."
            );

            return;
        }

        int courseId;

        try {

            courseId =
                    Integer.parseInt(courseIdText);

        } catch (NumberFormatException e) {

            response.getWriter().println(
                    "Invalid course ID."
            );

            return;
        }

        String sql =
                "INSERT INTO lessons " +
                "(course_id, lesson_title, lesson_content) " +
                "VALUES (?, ?, ?)";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, courseId);
            ps.setString(2, title.trim());
            ps.setString(3, content.trim());

            int rows = ps.executeUpdate();

            System.out.println(
                    "Lesson inserted. Rows affected = " + rows
            );

            response.sendRedirect(
                    "lms?action=lessons"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType(
                    "text/html;charset=UTF-8"
            );

            PrintWriter out = response.getWriter();

            out.println("<h2>Lesson Database Error</h2>");

            out.println("<pre>");

            e.printStackTrace(out);

            out.println("</pre>");
        }
    }

    // =========================================================
    // ENROLL STUDENT
    // =========================================================

    private void enrollStudent(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String studentIdText =
                request.getParameter("student_id");

        String courseIdText =
                request.getParameter("course_id");

        if (studentIdText == null ||
            studentIdText.trim().isEmpty() ||
            courseIdText == null ||
            courseIdText.trim().isEmpty()) {

            response.getWriter().println(
                    "Please select a student and a course."
            );

            return;
        }

        int studentId;
        int courseId;

        try {

            studentId =
                    Integer.parseInt(studentIdText);

            courseId =
                    Integer.parseInt(courseIdText);

        } catch (NumberFormatException e) {

            response.getWriter().println(
                    "Invalid student or course ID."
            );

            return;
        }

        String sql =
                "INSERT INTO enrollments " +
                "(student_id, course_id) " +
                "VALUES (?, ?)";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);

            int rows = ps.executeUpdate();

            System.out.println(
                    "Enrollment inserted. Rows affected = " +
                    rows
            );

            response.sendRedirect(
                    "lms?action=enrollments"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType(
                    "text/html;charset=UTF-8"
            );

            PrintWriter out = response.getWriter();

            out.println("<h2>Enrollment Database Error</h2>");

            out.println("<pre>");

            e.printStackTrace(out);

            out.println("</pre>");
        }
    }

    // =========================================================
    // COUNT RECORDS
    // =========================================================

    private int getCount(String table) {

        int count = 0;

        // Only allow our four known tables.
        if (!table.equals("courses") &&
            !table.equals("students") &&
            !table.equals("lessons") &&
            !table.equals("enrollments")) {

            return 0;
        }

        String sql =
                "SELECT COUNT(*) FROM " + table;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {

                count = rs.getInt(1);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return count;
    }

    // =========================================================
    // CARD
    // =========================================================

    private void card(
            PrintWriter out,
            String icon,
            String title,
            int number) {

        out.println("<div class='card'>");

        out.println(
                "<div class='card-icon'>" +
                icon +
                "</div>"
        );

        out.println(
                "<h3>" +
                number +
                "</h3>"
        );

        out.println(
                "<p>" +
                title +
                "</p>"
        );

        out.println("</div>");
    }

    // =========================================================
    // PAGE START
    // =========================================================

    private void pageStart(
            PrintWriter out,
            String title) {

        out.println("<!DOCTYPE html>");

        out.println("<html>");

        out.println("<head>");

        out.println(
                "<meta charset='UTF-8'>"
        );

        out.println(
                "<meta name='viewport' " +
                "content='width=device-width, initial-scale=1.0'>"
        );

        out.println(
                "<title>Code & Conquer | " +
                escapeHtml(title) +
                "</title>"
        );

        out.println(
                "<link rel='stylesheet' href='style.css'>"
        );

        out.println("</head>");

        out.println("<body>");

        // NAVBAR

        out.println("<nav>");

        out.println(
                "<div class='logo'>CODE<span>&</span>CONQUER</div>"
        );

        out.println("<div class='nav-links'>");

        out.println(
                "<a href='lms?action=dashboard'>Home</a>"
        );

        out.println(
                "<a href='lms?action=courses'>Courses</a>"
        );

        out.println(
                "<a href='lms?action=lessons'>Lessons</a>"
        );

        out.println(
                "<a href='lms?action=students'>Students</a>"
        );

        out.println(
                "<a href='lms?action=enrollments'>Enrollments</a>"
        );

        out.println("</div>");

        out.println("</nav>");

        out.println("<main>");
    }

    // =========================================================
    // PAGE END
    // =========================================================

    private void pageEnd(PrintWriter out) {

        out.println("</main>");

        out.println("<footer>");

        out.println(
                "<h3>CODE & CONQUER</h3>"
        );

        out.println(
                "<p>Learn. Code. Create. Conquer.</p>"
        );

        out.println(
                "<p>© 2026 Code & Conquer</p>"
        );

        out.println("</footer>");

        out.println("</body>");

        out.println("</html>");
    }

    // =========================================================
    // HTML ESCAPE
    // =========================================================

    private String escapeHtml(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}