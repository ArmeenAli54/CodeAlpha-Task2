import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

class HotelReservationSystem {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Data.init();
            new LoginFrame();
        });
    }

    // ================= COLOURS (shared) =================
    static final Color C_DARK     = new Color(10,  40,  30);
    static final Color C_PANEL    = new Color(15,  55,  40);
    static final Color C_ALT      = new Color(20,  70,  50);
    static final Color C_BORDER   = new Color(30,  80,  55);
    static final Color C_GOLD     = new Color(212, 175, 55);
    static final Color C_GREEN    = new Color(20,  90,  60);
    static final Color C_MUTED    = new Color(100, 140, 110);
    static final Color C_WHITE    = Color.WHITE;

    // ================= USER =================
    static class User {
        String u, p, r;
        User(String u, String p, String r) { this.u=u; this.p=p; this.r=r; }
    }

    // ================= ROOM =================
    static class Room {
        int id; String type, features; double price; boolean available;
        Room(int id, String type, String features, double price, boolean available) {
            this.id=id; this.type=type; this.features=features;
            this.price=price; this.available=available;
        }
    }

    // ================= BOOKING =================
    static class Booking {
        String guestName, checkIn, checkOut, roomType; int roomId;
        Booking(String guestName, String checkIn, String checkOut, int roomId, String roomType) {
            this.guestName=guestName; this.checkIn=checkIn;
            this.checkOut=checkOut; this.roomId=roomId; this.roomType=roomType;
        }
        @Override public String toString() {
            return "Guest: "+guestName+" | Room "+roomId+" ("+roomType+")"
                  +" | Check-In: "+checkIn+" | Check-Out: "+checkOut;
        }
    }

    // ================= DATA =================
    static class Data {
        static ArrayList<User>    users    = new ArrayList<>();
        static ArrayList<Room>    rooms    = new ArrayList<>();
        static ArrayList<Booking> bookings = new ArrayList<>();
        static void init() {
            users.add(new User("admin","admin","ADMIN"));
            users.add(new User("user","user","USER"));
            rooms.add(new Room(101,"Standard","Single Bed | WiFi | AC",50,true));
            rooms.add(new Room(201,"Deluxe","Double Bed | WiFi | TV | Minibar",100,true));
            rooms.add(new Room(301,"Suite","King Bed | Living Area | Smart TV | Kitchen",200,true));
        }
    }

    // =====================================================================
    //  SHARED HELPERS
    // =====================================================================

    /** Gold-styled label */
    static JLabel goldLabel(String text, int size, int style) {
        JLabel l = new JLabel(text);
        l.setForeground(C_GOLD);
        l.setFont(new Font("Serif", style, size));
        return l;
    }

    /** White label */
    static JLabel whiteLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(C_WHITE);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return l;
    }

    /** Themed text field */
    static JTextField themedField(String placeholder) {
        JTextField f = new JTextField(placeholder);
        f.setBackground(C_PANEL);
        f.setForeground(C_WHITE);
        f.setCaretColor(C_WHITE);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(C_BORDER, 1),
                new EmptyBorder(4, 8, 4, 8)));
        return f;
    }

    /** Themed password field */
    static JPasswordField themedPass() {
        JPasswordField f = new JPasswordField();
        f.setBackground(C_PANEL);
        f.setForeground(C_WHITE);
        f.setCaretColor(C_WHITE);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(C_BORDER, 1),
                new EmptyBorder(4, 8, 4, 8)));
        return f;
    }

    /** Themed combo box */
    static JComboBox<String> themedCombo(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setBackground(C_PANEL);
        cb.setForeground(C_WHITE);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cb.setBorder(new LineBorder(C_BORDER, 1));
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(
                    JList<?> list, Object val, int idx, boolean sel, boolean foc) {
                super.getListCellRendererComponent(list,val,idx,sel,foc);
                setBackground(sel ? C_ALT : C_PANEL);
                setForeground(C_WHITE);
                setBorder(new EmptyBorder(4,8,4,8));
                return this;
            }
        });
        return cb;
    }

    /** Gold action button */
    static JButton goldBtn(String text) {
        JButton b = new JButton(text);
        b.setBackground(C_GOLD);
        b.setForeground(Color.BLACK);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(8, 18, 8, 18));
        return b;
    }

    /** Dark secondary button (used for BACK) */
    static JButton backBtn() {
        JButton b = new JButton("\u2190  BACK");
        b.setBackground(C_PANEL);
        b.setForeground(C_GOLD);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(C_GOLD, 1),
                new EmptyBorder(6, 14, 6, 14)));
        return b;
    }

    // ---- placeholder ----
    static void showPlaceholder(JPanel area) {
        area.removeAll();
        area.setLayout(new GridBagLayout());
        area.setBackground(C_DARK);
        JLabel lbl = new JLabel("Select an option to view details", SwingConstants.CENTER);
        lbl.setForeground(C_MUTED);
        lbl.setFont(new Font("Serif", Font.ITALIC, 16));
        area.add(lbl);
        area.revalidate();
        area.repaint();
    }

    // ---- table renderer ----
    static void renderTable(JPanel area, String title, String[] columns, Object[][] rows) {
        area.removeAll();
        area.setLayout(new BorderLayout(0, 0));
        area.setBackground(C_DARK);

        // top bar: title + back button
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(C_DARK);
        topBar.setBorder(new EmptyBorder(10, 12, 6, 12));

        JLabel lbl = goldLabel(title, 17, Font.BOLD);
        lbl.setHorizontalAlignment(SwingConstants.LEFT);
        topBar.add(lbl, BorderLayout.CENTER);

        JButton back = backBtn();
        back.addActionListener(e -> showPlaceholder(area));
        topBar.add(back, BorderLayout.EAST);

        area.add(topBar, BorderLayout.NORTH);

        // table
        DefaultTableModel model = new DefaultTableModel(rows, columns) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setBackground(C_PANEL);
        table.setForeground(C_WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(32);
        table.setGridColor(new Color(40, 100, 70));
        table.setShowGrid(true);
        table.setSelectionBackground(new Color(212, 175, 55, 140));
        table.setSelectionForeground(Color.BLACK);
        table.setFocusable(false);
        table.setIntercellSpacing(new Dimension(1, 1));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t,val,sel,foc,row,col);
                setHorizontalAlignment(SwingConstants.CENTER);
                setBorder(new EmptyBorder(0,8,0,8));
                if (sel) { setBackground(new Color(212,175,55,160)); setForeground(Color.BLACK); }
                else     { setBackground(row%2==0 ? C_PANEL : C_ALT); setForeground(C_WHITE); }
                return this;
            }
        });

        JTableHeader header = table.getTableHeader();
        header.setBackground(C_GOLD);
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getWidth(), 36));
        header.setReorderingAllowed(false);
        ((DefaultTableCellRenderer) header.getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        for (int c = 0; c < table.getColumnCount(); c++) {
            int maxW = 80;
            for (int r = 0; r < table.getRowCount(); r++) {
                Component comp = table.prepareRenderer(table.getCellRenderer(r,c), r, c);
                maxW = Math.max(comp.getPreferredSize().width + 20, maxW);
            }
            table.getColumnModel().getColumn(c).setPreferredWidth(maxW);
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(C_PANEL);
        scroll.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(0, 12, 12, 12),
                new LineBorder(C_GOLD, 1)));

        area.add(scroll, BorderLayout.CENTER);
        area.revalidate();
        area.repaint();
    }

    // ---- themed form panel builder ----
    // Returns a JPanel with the dark-themed form already laid out.
    // Caller adds their own fields/buttons.
    static JPanel formPanel(String title, JPanel area) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 0));
        panel.setBackground(C_DARK);

        // top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(C_DARK);
        topBar.setBorder(new EmptyBorder(10, 12, 6, 12));
        JLabel lbl = goldLabel(title, 17, Font.BOLD);
        topBar.add(lbl, BorderLayout.CENTER);
        JButton back = backBtn();
        back.addActionListener(e -> showPlaceholder(area));
        topBar.add(back, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        return panel;
    }

    // =====================================================================
    //  LOGIN
    // =====================================================================
    static class LoginFrame extends JFrame {
        JTextField     u = new JTextField();
        JPasswordField p = new JPasswordField();

        LoginFrame() {
            setTitle("Royal Luxury Hotel");
            setSize(950, 600);
            setLayout(null);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            getContentPane().setBackground(C_DARK);

            JPanel left = new JPanel(null);
            left.setBounds(0, 0, 380, 600);
            left.setBackground(C_GREEN);
            add(left);

            JLabel brand = new JLabel("ROYAL LUXURY HOTEL");
            brand.setBounds(40, 220, 400, 40);
            brand.setForeground(C_WHITE);
            brand.setFont(new Font("Serif", Font.BOLD, 22));
            left.add(brand);

            JLabel tag = new JLabel("Where Comfort Meets Luxury");
            tag.setBounds(55, 260, 300, 30);
            tag.setForeground(C_GOLD);
            left.add(tag);

            JPanel right = new JPanel(null);
            right.setBounds(380, 0, 570, 600);
            right.setBackground(C_DARK);
            add(right);

            JLabel title = new JLabel("LOGIN PORTAL");
            title.setBounds(180, 70, 300, 40);
            title.setForeground(C_GOLD);
            title.setFont(new Font("Serif", Font.BOLD, 28));
            right.add(title);

            JLabel ul = new JLabel("USERNAME");
            ul.setBounds(140, 160, 200, 20);
            ul.setForeground(C_WHITE);
            right.add(ul);
            u.setBounds(140, 180, 300, 40);
            u.setBackground(C_PANEL); u.setForeground(C_WHITE); u.setCaretColor(C_WHITE);
            u.setBorder(BorderFactory.createCompoundBorder(new LineBorder(C_BORDER,1), new EmptyBorder(4,8,4,8)));
            right.add(u);

            JLabel pl = new JLabel("PASSWORD");
            pl.setBounds(140, 240, 200, 20);
            pl.setForeground(C_WHITE);
            right.add(pl);
            p.setBounds(140, 260, 300, 40);
            p.setBackground(C_PANEL); p.setForeground(C_WHITE); p.setCaretColor(C_WHITE);
            p.setBorder(BorderFactory.createCompoundBorder(new LineBorder(C_BORDER,1), new EmptyBorder(4,8,4,8)));
            right.add(p);

            JButton userBtn  = goldBtn("USER LOGIN");
            JButton adminBtn = goldBtn("ADMIN LOGIN");
            userBtn.setBounds(140, 330, 145, 45);
            adminBtn.setBounds(295, 330, 145, 45);
            right.add(userBtn);
            right.add(adminBtn);

            userBtn.addActionListener(e  -> login("USER"));
            adminBtn.addActionListener(e -> login("ADMIN"));
            setVisible(true);
        }

        void login(String role) {
            for (User us : Data.users) {
                if (us.u.equals(u.getText().trim())
                        && us.p.equals(new String(p.getPassword()).trim())
                        && us.r.equals(role)) {
                    dispose();
                    if (role.equals("USER")) new UserPanel(us);
                    else                     new AdminPanel(us);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =====================================================================
    //  USER PANEL
    // =====================================================================
    static class UserPanel extends JFrame {

        JPanel tableArea = new JPanel(new BorderLayout());

        UserPanel(User u) {
            setTitle("User Panel – " + u.u);
            setSize(1100, 620);
            setLayout(null);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            getContentPane().setBackground(C_DARK);

            JLabel head = goldLabel("USER DASHBOARD", 20, Font.BOLD);
            head.setBounds(20, 18, 240, 30);
            add(head);

            JButton view   = goldBtn("VIEW ROOMS");
            JButton book   = goldBtn("BOOK ROOM");
            JButton search = goldBtn("SEARCH ROOM");
            JButton cancel = goldBtn("CANCEL BOOKING");
            JButton logout = goldBtn("LOGOUT");

            view.setBounds(20, 80,  220, 40);
            book.setBounds(20, 135, 220, 40);
            search.setBounds(20, 190, 220, 40);
            cancel.setBounds(20, 245, 220, 40);
            logout.setBounds(20, 300, 220, 40);

            add(view); add(book); add(search); add(cancel); add(logout);

            tableArea.setBounds(260, 10, 820, 580);
            tableArea.setBackground(C_DARK);
            tableArea.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(C_BORDER, 1), new EmptyBorder(4,4,4,4)));
            add(tableArea);

            showPlaceholder(tableArea);

            view.addActionListener(e   -> showRooms());
            search.addActionListener(e -> showSearchForm());
            book.addActionListener(e   -> showBookingForm());
            cancel.addActionListener(e -> showCancelForm());
            logout.addActionListener(e -> { dispose(); new LoginFrame(); });

            setVisible(true);
        }

        // ---- VIEW ROOMS ----
        void showRooms() {
            if (Data.rooms.isEmpty()) { showPlaceholder(tableArea); return; }
            String[] cols = {"Room ID","Type","Features","Price / Night","Status"};
            Object[][] rows = new Object[Data.rooms.size()][5];
            for (int i = 0; i < Data.rooms.size(); i++) {
                Room r = Data.rooms.get(i);
                rows[i] = new Object[]{ r.id, r.type, r.features,
                        "$"+(int)r.price, r.available ? "Available" : "Booked" };
            }
            renderTable(tableArea, "All Rooms", cols, rows);
        }

        // ---- SEARCH FORM (themed, inline) ----
        void showSearchForm() {
            JPanel fp = formPanel("Search Room", tableArea);

            JPanel body = new JPanel(new GridBagLayout());
            body.setBackground(C_DARK);
            GridBagConstraints g = new GridBagConstraints();
            g.insets = new Insets(10, 20, 10, 20);
            g.fill = GridBagConstraints.HORIZONTAL;
            g.gridx = 0; g.gridy = 0;

            JLabel info = whiteLabel("Enter room type to search:");
            info.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            body.add(info, g);

            g.gridy = 1;
            JTextField typeField = themedField("");
            typeField.setPreferredSize(new Dimension(300, 38));
            body.add(typeField, g);

            // hint
            g.gridy = 2;
            JLabel hint = new JLabel("Options: Standard / Deluxe / Suite");
            hint.setForeground(C_MUTED);
            hint.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            body.add(hint, g);

            g.gridy = 3;
            JButton searchBtn = goldBtn("SEARCH");
            searchBtn.setPreferredSize(new Dimension(300, 40));
            body.add(searchBtn, g);

            fp.add(body, BorderLayout.CENTER);
            tableArea.removeAll();
            tableArea.setLayout(new BorderLayout());
            tableArea.add(fp, BorderLayout.CENTER);
            tableArea.revalidate();
            tableArea.repaint();

            searchBtn.addActionListener(e -> {
                String t = typeField.getText().trim();
                if (t.isEmpty()) return;
                ArrayList<Room> found = new ArrayList<>();
                for (Room r : Data.rooms)
                    if (r.type.equalsIgnoreCase(t)) found.add(r);
                if (found.isEmpty()) {
                    showPlaceholder(tableArea);
                    JOptionPane.showMessageDialog(this, "No rooms found for type: " + t);
                    return;
                }
                String[] cols = {"Room ID","Type","Features","Price / Night","Status"};
                Object[][] rows = new Object[found.size()][5];
                for (int i = 0; i < found.size(); i++) {
                    Room r = found.get(i);
                    rows[i] = new Object[]{ r.id, r.type, r.features,
                            "$"+(int)r.price, r.available ? "Available" : "Booked" };
                }
                renderTable(tableArea, "Search Results – " + t, cols, rows);
            });
        }

        // ---- BOOKING FORM (themed, inline) ----
        void showBookingForm() {
            ArrayList<Room> available = new ArrayList<>();
            for (Room r : Data.rooms) if (r.available) available.add(r);
            if (available.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No rooms are currently available.");
                return;
            }

            JPanel fp = formPanel("Book a Room", tableArea);

            JPanel body = new JPanel(new GridBagLayout());
            body.setBackground(C_DARK);
            GridBagConstraints g = new GridBagConstraints();
            g.insets = new Insets(8, 30, 8, 30);
            g.fill = GridBagConstraints.HORIZONTAL;
            g.gridwidth = 2;

            String[] roomOptions = new String[available.size()];
            for (int i = 0; i < available.size(); i++)
                roomOptions[i] = "Room "+available.get(i).id+" – "+available.get(i).type
                        +" ($"+(int)available.get(i).price+"/night)";

            JTextField nameField    = themedField("");
            JTextField checkInField = themedField("DD/MM/YYYY");
            JTextField checkOutField= themedField("DD/MM/YYYY");
            JComboBox<String> roomBox = themedCombo(roomOptions);

            String[][] fields = {
                {"Guest Name",  null},
                {"Check-In",    null},
                {"Check-Out",   null},
                {"Select Room", null}
            };
            JComponent[] inputs = { nameField, checkInField, checkOutField, roomBox };

            for (int i = 0; i < inputs.length; i++) {
                g.gridy = i * 2; g.gridx = 0;
                body.add(whiteLabel(fields[i][0] + ":"), g);
                g.gridy = i * 2 + 1;
                inputs[i].setPreferredSize(new Dimension(360, 38));
                body.add(inputs[i], g);
            }

            g.gridy = inputs.length * 2; g.gridx = 0;
            JButton confirmBtn = goldBtn("CONFIRM BOOKING");
            confirmBtn.setPreferredSize(new Dimension(360, 42));
            body.add(confirmBtn, g);

            JScrollPane bodyScroll = new JScrollPane(body);
            bodyScroll.setBackground(C_DARK);
            bodyScroll.getViewport().setBackground(C_DARK);
            bodyScroll.setBorder(BorderFactory.createEmptyBorder());

            fp.add(bodyScroll, BorderLayout.CENTER);
            tableArea.removeAll();
            tableArea.setLayout(new BorderLayout());
            tableArea.add(fp, BorderLayout.CENTER);
            tableArea.revalidate();
            tableArea.repaint();

            confirmBtn.addActionListener(e -> {
                String name     = nameField.getText().trim();
                String checkIn  = checkInField.getText().trim();
                String checkOut = checkOutField.getText().trim();
                if (name.isEmpty() || checkIn.isEmpty() || checkOut.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Room chosen = available.get(roomBox.getSelectedIndex());
                chosen.available = false;
                Booking b = new Booking(name, checkIn, checkOut, chosen.id, chosen.type);
                Data.bookings.add(b);
                String[] cols = {"Guest Name","Room ID","Room Type","Check-In","Check-Out","Charge"};
                Object[][] rows = {{ name, chosen.id, chosen.type, checkIn, checkOut,
                        "$"+(int)chosen.price+"/night  \u2714 Paid" }};
                renderTable(tableArea, "Booking Confirmed!", cols, rows);
            });
        }

        // ---- CANCEL FORM (themed, inline) ----
        void showCancelForm() {
            if (Data.bookings.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No active bookings to cancel.");
                return;
            }

            JPanel fp = formPanel("Cancel Booking", tableArea);

            JPanel body = new JPanel(new GridBagLayout());
            body.setBackground(C_DARK);
            GridBagConstraints g = new GridBagConstraints();
            g.insets = new Insets(10, 30, 10, 30);
            g.fill = GridBagConstraints.HORIZONTAL;
            g.gridx = 0;

            g.gridy = 0;
            body.add(whiteLabel("Select booking to cancel:"), g);

            String[] options = new String[Data.bookings.size()];
            for (int i = 0; i < Data.bookings.size(); i++)
                options[i] = Data.bookings.get(i).toString();
            JComboBox<String> bookingBox = themedCombo(options);
            bookingBox.setPreferredSize(new Dimension(500, 38));
            g.gridy = 1;
            body.add(bookingBox, g);

            g.gridy = 2;
            JButton cancelBtn = goldBtn("CANCEL SELECTED BOOKING");
            cancelBtn.setPreferredSize(new Dimension(500, 42));
            body.add(cancelBtn, g);

            fp.add(body, BorderLayout.CENTER);
            tableArea.removeAll();
            tableArea.setLayout(new BorderLayout());
            tableArea.add(fp, BorderLayout.CENTER);
            tableArea.revalidate();
            tableArea.repaint();

            cancelBtn.addActionListener(e -> {
                String chosen = (String) bookingBox.getSelectedItem();
                if (chosen == null) return;
                for (int i = 0; i < Data.bookings.size(); i++) {
                    if (Data.bookings.get(i).toString().equals(chosen)) {
                        Booking b = Data.bookings.remove(i);
                        for (Room r : Data.rooms)
                            if (r.id == b.roomId) { r.available = true; break; }
                        JOptionPane.showMessageDialog(this, "Booking cancelled successfully.");
                        showPlaceholder(tableArea);
                        return;
                    }
                }
            });
        }
    }

    // =====================================================================
    //  ADMIN PANEL
    // =====================================================================
    static class AdminPanel extends JFrame {

        JPanel tableArea = new JPanel(new BorderLayout());

        AdminPanel(User u) {
            setTitle("Admin Panel – " + u.u);
            setSize(1100, 620);
            setLayout(null);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            getContentPane().setBackground(C_DARK);

            JLabel head = goldLabel("ADMIN MANAGEMENT", 20, Font.BOLD);
            head.setBounds(20, 18, 240, 30);
            add(head);

            JButton viewBookings = goldBtn("VIEW BOOKINGS");
            JButton addRoomBtn   = goldBtn("ADD ROOM");
            JButton updateRoom   = goldBtn("UPDATE ROOM");
            JButton logout       = goldBtn("LOGOUT");

            viewBookings.setBounds(20, 80,  220, 40);
            addRoomBtn.setBounds(20, 135, 220, 40);
            updateRoom.setBounds(20, 190, 220, 40);
            logout.setBounds(20, 245, 220, 40);

            add(viewBookings); add(addRoomBtn); add(updateRoom); add(logout);

            tableArea.setBounds(260, 10, 820, 580);
            tableArea.setBackground(C_DARK);
            tableArea.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(C_BORDER, 1), new EmptyBorder(4,4,4,4)));
            add(tableArea);

            showPlaceholder(tableArea);

            viewBookings.addActionListener(e -> showAllBookings());
            addRoomBtn.addActionListener(e   -> showAddRoomForm());
            updateRoom.addActionListener(e   -> showUpdateRoomForm());
            logout.addActionListener(e       -> { dispose(); new LoginFrame(); });

            setVisible(true);
        }

        // ---- VIEW BOOKINGS ----
        void showAllBookings() {
            if (Data.bookings.isEmpty()) {
                showPlaceholder(tableArea);
                JOptionPane.showMessageDialog(this, "No bookings found.");
                return;
            }
            String[] cols = {"Guest Name","Room ID","Room Type","Check-In","Check-Out"};
            Object[][] rows = new Object[Data.bookings.size()][5];
            for (int i = 0; i < Data.bookings.size(); i++) {
                Booking b = Data.bookings.get(i);
                rows[i] = new Object[]{ b.guestName, b.roomId, b.roomType, b.checkIn, b.checkOut };
            }
            renderTable(tableArea, "All Bookings", cols, rows);
        }

        // ---- ADD ROOM FORM (themed, inline) ----
        void showAddRoomForm() {
            JPanel fp = formPanel("Add New Room", tableArea);

            JPanel body = new JPanel(new GridBagLayout());
            body.setBackground(C_DARK);
            GridBagConstraints g = new GridBagConstraints();
            g.insets = new Insets(8, 30, 8, 30);
            g.fill = GridBagConstraints.HORIZONTAL;
            g.gridx = 0;

            JTextField idField       = themedField("");
            JTextField typeField     = themedField("");
            JTextField featuresField = themedField("");
            JTextField priceField    = themedField("");

            String[] labels = {"Room ID:", "Type:", "Features:", "Price / Night ($):"};
            JTextField[] inputs = { idField, typeField, featuresField, priceField };

            for (int i = 0; i < inputs.length; i++) {
                g.gridy = i * 2;
                body.add(whiteLabel(labels[i]), g);
                g.gridy = i * 2 + 1;
                inputs[i].setPreferredSize(new Dimension(360, 38));
                body.add(inputs[i], g);
            }

            g.gridy = inputs.length * 2;
            JButton addBtn = goldBtn("ADD ROOM");
            addBtn.setPreferredSize(new Dimension(360, 42));
            body.add(addBtn, g);

            fp.add(body, BorderLayout.CENTER);
            tableArea.removeAll();
            tableArea.setLayout(new BorderLayout());
            tableArea.add(fp, BorderLayout.CENTER);
            tableArea.revalidate();
            tableArea.repaint();

            addBtn.addActionListener(e -> {
                try {
                    int    id    = Integer.parseInt(idField.getText().trim());
                    String type  = typeField.getText().trim();
                    String feats = featuresField.getText().trim();
                    double price = Double.parseDouble(priceField.getText().trim());
                    if (type.isEmpty() || feats.isEmpty()) throw new IllegalArgumentException();
                    Data.rooms.add(new Room(id, type, feats, price, true));
                    String[] cols = {"Room ID","Type","Features","Price / Night","Status"};
                    Object[][] rows = {{ id, type, feats, "$"+(int)price, "Available" }};
                    renderTable(tableArea, "Room "+id+" Added Successfully", cols, rows);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid input. Please check all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        }

        // ---- UPDATE ROOM FORM (themed, inline) ----
        void showUpdateRoomForm() {
            if (Data.rooms.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No rooms available to update.");
                return;
            }

            JPanel fp = formPanel("Update Room", tableArea);

            JPanel body = new JPanel(new GridBagLayout());
            body.setBackground(C_DARK);
            GridBagConstraints g = new GridBagConstraints();
            g.insets = new Insets(8, 30, 8, 30);
            g.fill = GridBagConstraints.HORIZONTAL;
            g.gridx = 0;

            // Step 1: room selector
            String[] roomOptions = new String[Data.rooms.size()];
            for (int i = 0; i < Data.rooms.size(); i++)
                roomOptions[i] = "Room "+Data.rooms.get(i).id+" – "+Data.rooms.get(i).type;

            g.gridy = 0;
            body.add(whiteLabel("Select room to update:"), g);

            JComboBox<String> roomSelector = themedCombo(roomOptions);
            roomSelector.setPreferredSize(new Dimension(360, 38));
            g.gridy = 1;
            body.add(roomSelector, g);

            // Pre-fill fields based on selection
            Room first = Data.rooms.get(0);
            JTextField typeField     = themedField(first.type);
            JTextField featuresField = themedField(first.features);
            JTextField priceField    = themedField(String.valueOf(first.price));

            String[] editLabels = {"Type:", "Features:", "Price / Night ($):"};
            JTextField[] editInputs = { typeField, featuresField, priceField };

            for (int i = 0; i < editInputs.length; i++) {
                g.gridy = 2 + i * 2;
                body.add(whiteLabel(editLabels[i]), g);
                g.gridy = 3 + i * 2;
                editInputs[i].setPreferredSize(new Dimension(360, 38));
                body.add(editInputs[i], g);
            }

            g.gridy = 2 + editInputs.length * 2;
            JButton updateBtn = goldBtn("SAVE CHANGES");
            updateBtn.setPreferredSize(new Dimension(360, 42));
            body.add(updateBtn, g);

            // Update fields when selection changes
            roomSelector.addActionListener(e -> {
                Room sel = Data.rooms.get(roomSelector.getSelectedIndex());
                typeField.setText(sel.type);
                featuresField.setText(sel.features);
                priceField.setText(String.valueOf(sel.price));
            });

            fp.add(body, BorderLayout.CENTER);
            tableArea.removeAll();
            tableArea.setLayout(new BorderLayout());
            tableArea.add(fp, BorderLayout.CENTER);
            tableArea.revalidate();
            tableArea.repaint();

            updateBtn.addActionListener(e -> {
                try {
                    Room target = Data.rooms.get(roomSelector.getSelectedIndex());
                    target.type     = typeField.getText().trim();
                    target.features = featuresField.getText().trim();
                    target.price    = Double.parseDouble(priceField.getText().trim());
                    String[] cols = {"Room ID","Type","Features","Price / Night","Status"};
                    Object[][] rows = {{ target.id, target.type, target.features,
                            "$"+(int)target.price, target.available ? "Available" : "Booked" }};
                    renderTable(tableArea, "Room "+target.id+" Updated Successfully", cols, rows);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }
}