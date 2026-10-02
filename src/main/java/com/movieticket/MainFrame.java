package com.movieticket;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MainFrame extends JFrame {

    JComboBox<String> movieComboBox;
    JComboBox<String> showComboBox;

    Map<String, JToggleButton> seatButtonMap = new HashMap<>();
    Map<String, Integer> movieMap = new HashMap<>();
    Map<String, Integer> showMap = new HashMap<>();

    HashSet<String> bookedSeats = new HashSet<>();
    ArrayList<String> selectedSeats = new ArrayList<>();

    boolean loadingShows = false;

    JTextField nameField;
    JTextField phoneField;

    JTextArea billArea;

    String lastReceipt = "";

    public MainFrame() {

        setTitle("Movie Ticket Booking System");
        setSize(950, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(15, 15));

        getContentPane().setBackground(
                new Color(245, 247, 250)
        );

        // =====================================================
        // HEADER
        // =====================================================

        JPanel topPanel =
                new JPanel(new BorderLayout(10, 8));

        topPanel.setBackground(
                new Color(30, 35, 45)
        );

        topPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 20, 15, 20
                )
        );

        JLabel titleLabel =
                new JLabel(
                        "MOVIE TICKET BOOKING SYSTEM"
                );

        titleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        24
                )
        );

        titleLabel.setForeground(Color.WHITE);

        titleLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        JLabel subtitleLabel =
                new JLabel(
                        "Select a movie, choose a show and reserve your seats"
                );

        subtitleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        subtitleLabel.setForeground(
                new Color(210, 215, 225)
        );

        subtitleLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        JPanel headingPanel =
                new JPanel(
                        new GridLayout(2, 1)
                );

        headingPanel.setOpaque(false);

        headingPanel.add(titleLabel);
        headingPanel.add(subtitleLabel);

        // =====================================================
        // MOVIE AND SHOW SELECTION
        // =====================================================

        JPanel selectionPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                12,
                                8
                        )
                );

        selectionPanel.setOpaque(false);

        JLabel movieLabel =
                new JLabel("Movie:");

        movieLabel.setForeground(Color.WHITE);

        movieLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        movieComboBox =
                new JComboBox<>();

        movieComboBox.setPreferredSize(
                new Dimension(190, 32)
        );

        movieComboBox.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        JLabel showLabel =
                new JLabel("Show Time:");

        showLabel.setForeground(Color.WHITE);

        showLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        showComboBox =
                new JComboBox<>();

        showComboBox.setPreferredSize(
                new Dimension(150, 32)
        );

        showComboBox.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        selectionPanel.add(movieLabel);
        selectionPanel.add(movieComboBox);

        selectionPanel.add(showLabel);
        selectionPanel.add(showComboBox);

        topPanel.add(
                headingPanel,
                BorderLayout.NORTH
        );

        topPanel.add(
                selectionPanel,
                BorderLayout.SOUTH
        );

        add(
                topPanel,
                BorderLayout.NORTH
        );

        // =====================================================
        // SEAT AREA
        // =====================================================

        JPanel seatPanel =
                new JPanel(
                        new GridLayout(
                                4,
                                5,
                                12,
                                12
                        )
                );

        seatPanel.setBackground(
                new Color(245, 247, 250)
        );

        seatPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 35, 15, 35
                )
        );

        String[] seatNames = {

                "A1", "A2", "A3", "A4", "A5",

                "B1", "B2", "B3", "B4", "B5",

                "C1", "C2", "C3", "C4", "C5",

                "D1", "D2", "D3", "D4", "D5"
        };

        for (String seatName : seatNames) {

            JToggleButton seatButton =
                    new JToggleButton(
                            seatName
                    );

            seatButton.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            15
                    )
            );

            seatButton.setFocusPainted(false);

            seatButton.setOpaque(true);

            seatButton.setContentAreaFilled(true);

            seatButton.setBackground(Color.WHITE);

            seatButton.setForeground(
                    new Color(35, 40, 50)
            );

            seatButton.setBorder(
                    BorderFactory.createLineBorder(
                            new Color(
                                    190,
                                    195,
                                    205
                            ),
                            1
                    )
            );

            seatButtonMap.put(
                    seatName,
                    seatButton
            );

            // MouseListener required by specification

            seatButton.addMouseListener(
                    new MouseAdapter() {

                        @Override
                        public void mouseClicked(
                                MouseEvent e
                        ) {

                            toggleSeat(
                                    seatName
                            );
                        }
                    }
            );

            seatPanel.add(
                    seatButton
            );
        }

        JPanel seatAreaPanel =
                new JPanel(
                        new BorderLayout()
                );

        seatAreaPanel.setBackground(
                new Color(245, 247, 250)
        );

        JLabel screenLabel =
                new JLabel("SCREEN");

        screenLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        15
                )
        );

        screenLabel.setForeground(Color.WHITE);

        screenLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        screenLabel.setOpaque(true);

        screenLabel.setBackground(
                new Color(70, 75, 90)
        );

        screenLabel.setBorder(
                BorderFactory.createEmptyBorder(
                        8, 20, 8, 20
                )
        );

        seatAreaPanel.add(
                screenLabel,
                BorderLayout.NORTH
        );

        seatAreaPanel.add(
                seatPanel,
                BorderLayout.CENTER
        );

        add(
                seatAreaPanel,
                BorderLayout.CENTER
        );

        // =====================================================
        // BOTTOM AREA
        // =====================================================

        JPanel bottomMainPanel =
                new JPanel(
                        new BorderLayout(
                                12,
                                12
                        )
                );

        bottomMainPanel.setBackground(
                new Color(245, 247, 250)
        );

        bottomMainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        5,
                        20,
                        15,
                        20
                )
        );

        // =====================================================
        // CUSTOMER DETAILS
        // =====================================================

        JPanel customerPanel =
                new JPanel(
                        new GridLayout(
                                2,
                                2,
                                12,
                                10
                        )
                );

        customerPanel.setBackground(Color.WHITE);

        customerPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Customer Details"
                )
        );

        JLabel nameLabel =
                new JLabel("Customer Name:");

        nameLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        nameField =
                new JTextField();

        styleTextField(nameField);

        JLabel phoneLabel =
                new JLabel("Phone Number:");

        phoneLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        phoneField =
                new JTextField();

        styleTextField(phoneField);

        customerPanel.add(nameLabel);
        customerPanel.add(nameField);

        customerPanel.add(phoneLabel);
        customerPanel.add(phoneField);

        bottomMainPanel.add(
                customerPanel,
                BorderLayout.NORTH
        );

        // =====================================================
        // BUTTON PANEL
        // =====================================================

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                5
                        )
                );

        buttonPanel.setBackground(
                new Color(245, 247, 250)
        );

        JButton bookButton =
                new JButton("BOOK TICKET");

        stylePrimaryButton(bookButton);

        JButton clearButton =
                new JButton("CLEAR");

        styleSecondaryButton(clearButton);

        JButton viewBookingsButton =
                new JButton("VIEW BOOKINGS");

        styleSecondaryButton(
                viewBookingsButton
        );

        JButton downloadButton =
                new JButton("DOWNLOAD TICKET");

        styleSecondaryButton(
                downloadButton
        );

        buttonPanel.add(bookButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(viewBookingsButton);
        buttonPanel.add(downloadButton);

        bottomMainPanel.add(
                buttonPanel,
                BorderLayout.CENTER
        );

        // =====================================================
        // BILL AREA
        // =====================================================

        billArea =
                new JTextArea();

        billArea.setEditable(false);

        billArea.setLineWrap(true);

        billArea.setWrapStyleWord(true);

        billArea.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        13
                )
        );

        billArea.setBackground(Color.WHITE);

        billArea.setBorder(
                BorderFactory.createTitledBorder(
                        "Booking Receipt"
                )
        );

        JScrollPane billScrollPane =
                new JScrollPane(
                        billArea
                );

        billScrollPane.setPreferredSize(
                new Dimension(
                        0,
                        180
                )
        );

        bottomMainPanel.add(
                billScrollPane,
                BorderLayout.SOUTH
        );

        add(
                bottomMainPanel,
                BorderLayout.SOUTH
        );

        // =====================================================
        // LOAD MOVIES
        // =====================================================

        loadMovies();

        // =====================================================
        // MOVIE SELECTION
        // =====================================================

        movieComboBox.addItemListener(
                e -> {

                    if (
                            e.getStateChange()
                                    == ItemEvent.SELECTED
                    ) {

                        String selectedMovie =
                                (String)
                                        movieComboBox
                                                .getSelectedItem();

                        if (
                                selectedMovie != null
                        ) {

                            Integer movieId =
                                    movieMap.get(
                                            selectedMovie
                                    );

                            if (
                                    movieId != null
                            ) {

                                loadShows(
                                        movieId
                                );
                            }
                        }
                    }
                }
        );

        // =====================================================
        // SHOW SELECTION
        // =====================================================

        showComboBox.addItemListener(
                e -> {

                    if (
                            e.getStateChange()
                                    == ItemEvent.SELECTED
                    ) {

                        if (loadingShows) {
                            return;
                        }

                        String selectedShow =
                                (String)
                                        showComboBox
                                                .getSelectedItem();

                        if (
                                selectedShow != null
                        ) {

                            Integer showId =
                                    showMap.get(
                                            selectedShow
                                    );

                            if (
                                    showId != null
                            ) {

                                loadBookedSeats(
                                        showId
                                );
                            }
                        }
                    }
                }
        );

        // =====================================================
        // BOOK BUTTON
        // =====================================================

        bookButton.addActionListener(
                e -> bookTickets()
        );

        // =====================================================
        // CLEAR BUTTON
        // =====================================================

        clearButton.addActionListener(
                e -> {

                    clearSelectedSeats();
                    clearCustomerFields();

                    billArea.setText("");
                }
        );

        // =====================================================
        // VIEW BOOKINGS
        // =====================================================

        viewBookingsButton.addActionListener(
                e -> showBookings()
        );

        // =====================================================
        // DOWNLOAD TICKET
        // =====================================================

        downloadButton.addActionListener(
                e -> downloadTicket()
        );
    }

    // =========================================================
    // TEXT FIELD STYLE
    // =========================================================

    private void styleTextField(
            JTextField field
    ) {

        field.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        200,
                                        205,
                                        215
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                5,
                                8,
                                5,
                                8
                        )
                )
        );
    }

    // =========================================================
    // PRIMARY BUTTON STYLE
    // =========================================================

    private void stylePrimaryButton(
            JButton button
    ) {

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        button.setForeground(Color.WHITE);

        button.setBackground(
                new Color(
                        40,
                        100,
                        180
                )
        );

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        18,
                        10,
                        18
                )
        );
    }

    // =========================================================
    // SECONDARY BUTTON STYLE
    // =========================================================

    private void styleSecondaryButton(
            JButton button
    ) {

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(
                new Color(
                        40,
                        45,
                        55
                )
        );

        button.setBackground(Color.WHITE);

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createLineBorder(
                        new Color(
                                180,
                                185,
                                195
                        )
                )
        );
    }

    // =========================================================
    // LOAD MOVIES
    // =========================================================

    private void loadMovies() {

        movieComboBox.removeAllItems();

        movieMap.clear();

        String sql =
                "SELECT movie_id, movie_name " +
                        "FROM movies " +
                        "ORDER BY movie_id";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (
                    resultSet.next()
            ) {

                int movieId =
                        resultSet.getInt(
                                "movie_id"
                        );

                String movieName =
                        resultSet.getString(
                                "movie_name"
                        );

                movieMap.put(
                        movieName,
                        movieId
                );

                movieComboBox.addItem(
                        movieName
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading movies:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // LOAD SHOWS
    // =========================================================

    private void loadShows(
            int movieId
    ) {

        loadingShows = true;

        showComboBox.removeAllItems();

        showMap.clear();

        String sql =
                "SELECT show_id, show_time " +
                        "FROM shows " +
                        "WHERE movie_id = ? " +
                        "ORDER BY show_id";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    movieId
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (
                        resultSet.next()
                ) {

                    int showId =
                            resultSet.getInt(
                                    "show_id"
                            );

                    String showTime =
                            resultSet.getString(
                                    "show_time"
                            );

                    showMap.put(
                            showTime,
                            showId
                    );

                    showComboBox.addItem(
                            showTime
                    );
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading shows:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        loadingShows = false;

        if (
                showComboBox.getItemCount()
                        > 0
        ) {

            showComboBox.setSelectedIndex(
                    0
            );

            String selectedShow =
                    (String)
                            showComboBox
                                    .getSelectedItem();

            Integer showId =
                    showMap.get(
                            selectedShow
                    );

            if (
                    showId != null
            ) {

                loadBookedSeats(
                        showId
                );
            }
        }
    }

    // =========================================================
    // LOAD BOOKED SEATS
    // =========================================================

    private void loadBookedSeats(
            int showId
    ) {

        bookedSeats.clear();

        selectedSeats.clear();

        for (
                String seatName :
                seatButtonMap.keySet()
        ) {

            JToggleButton button =
                    seatButtonMap.get(
                            seatName
                    );

            button.setEnabled(true);

            button.setSelected(false);

            button.setText(
                    seatName
            );

            button.setBackground(
                    Color.WHITE
            );

            button.setForeground(
                    new Color(
                            35,
                            40,
                            50
                    )
            );

            button.setBorder(
                    BorderFactory.createLineBorder(
                            new Color(
                                    190,
                                    195,
                                    205
                            )
                    )
            );
        }

        String sql =
                "SELECT seat_code " +
                        "FROM bookings " +
                        "WHERE show_id = ?";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    showId
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (
                        resultSet.next()
                ) {

                    String seatCode =
                            resultSet.getString(
                                    "seat_code"
                            );

                    bookedSeats.add(
                            seatCode
                    );
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading booked seats:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        for (
                String seatName :
                bookedSeats
        ) {

            JToggleButton button =
                    seatButtonMap.get(
                            seatName
                    );

            if (
                    button != null
            ) {

                button.setEnabled(false);

                button.setSelected(false);

                button.setBackground(
                        new Color(
                                215,
                                215,
                                215
                        )
                );

                button.setForeground(
                        new Color(
                                130,
                                130,
                                130
                        )
                );

                button.setText(
                        seatName + "  X"
                );
            }
        }
    }

    // =========================================================
    // TOGGLE SEAT
    // =========================================================

    private void toggleSeat(
            String seatName
    ) {

        if (
                bookedSeats.contains(
                        seatName
                )
        ) {

            return;
        }

        JToggleButton button =
                seatButtonMap.get(
                        seatName
                );

        if (
                button == null
        ) {

            return;
        }

        if (
                selectedSeats.contains(
                        seatName
                )
        ) {

            selectedSeats.remove(
                    seatName
            );

            button.setSelected(false);

            button.setBackground(
                    Color.WHITE
            );

            button.setForeground(
                    new Color(
                            35,
                            40,
                            50
                    )
            );

        } else {

            selectedSeats.add(
                    seatName
            );

            button.setSelected(true);

            button.setBackground(
                    new Color(
                            45,
                            120,
                            210
                    )
            );

            button.setForeground(
                    Color.WHITE
            );
        }
    }

    // =========================================================
    // BOOK TICKETS
    // =========================================================

    private void bookTickets() {

        String customerName =
                nameField.getText()
                        .trim();

        String customerPhone =
                phoneField.getText()
                        .trim();

        String selectedMovie =
                (String)
                        movieComboBox
                                .getSelectedItem();

        String selectedShow =
                (String)
                        showComboBox
                                .getSelectedItem();

        if (
                customerName.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter customer name.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (
                !customerPhone.matches(
                        "\\d{10}"
                )
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Phone number must contain exactly 10 digits.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (
                selectedMovie == null
                        ||
                        selectedShow == null
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select movie and show time.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (
                selectedSeats.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select at least one seat.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Integer showId =
                showMap.get(
                        selectedShow
                );

        if (
                showId == null
        ) {

            return;
        }

        String insertSql =
                "INSERT INTO bookings " +
                        "(customer_name, customer_phone, show_id, seat_code) " +
                        "VALUES (?, ?, ?, ?)";

        String checkSql =
                "SELECT COUNT(*) " +
                        "FROM bookings " +
                        "WHERE show_id = ? " +
                        "AND seat_code = ?";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement checkStatement =
                        connection.prepareStatement(
                                checkSql
                        );

                PreparedStatement insertStatement =
                        connection.prepareStatement(
                                insertSql
                        )
        ) {

            connection.setAutoCommit(false);

            // =================================================
            // CHECK CLASH
            // =================================================

            for (
                    String seatName :
                    selectedSeats
            ) {

                if (
                        bookedSeats.contains(
                                seatName
                        )
                ) {

                    connection.rollback();

                    JOptionPane.showMessageDialog(
                            this,
                            "Seat "
                                    + seatName
                                    + " is already booked.",
                            "Booking Error",
                            JOptionPane.ERROR_MESSAGE
                    );

                    loadBookedSeats(
                            showId
                    );

                    return;
                }

                checkStatement.setInt(
                        1,
                        showId
                );

                checkStatement.setString(
                        2,
                        seatName
                );

                try (
                        ResultSet resultSet =
                                checkStatement.executeQuery()
                ) {

                    if (
                            resultSet.next()
                    ) {

                        int count =
                                resultSet.getInt(
                                        1
                                );

                        if (
                                count > 0
                        ) {

                            connection.rollback();

                            JOptionPane.showMessageDialog(
                                    this,
                                    "Seat "
                                            + seatName
                                            + " is already booked.",
                                    "Booking Error",
                                    JOptionPane.ERROR_MESSAGE
                            );

                            loadBookedSeats(
                                    showId
                            );

                            return;
                        }
                    }
                }
            }

            // =================================================
            // INSERT ALL SELECTED SEATS
            // =================================================

            for (
                    String seatName :
                    selectedSeats
            ) {

                insertStatement.setString(
                        1,
                        customerName
                );

                insertStatement.setString(
                        2,
                        customerPhone
                );

                insertStatement.setInt(
                        3,
                        showId
                );

                insertStatement.setString(
                        4,
                        seatName
                );

                insertStatement.addBatch();
            }

            insertStatement.executeBatch();

            connection.commit();

            // =================================================
            // CREATE RECEIPT
            // =================================================

            StringBuilder receipt =
                    new StringBuilder();

            receipt.append(
                    "========================================\n"
            );

            receipt.append(
                    "        MOVIE TICKET BOOKING\n"
            );

            receipt.append(
                    "========================================\n\n"
            );

            receipt.append(
                    "Customer Name : "
            );

            receipt.append(
                    customerName
            );

            receipt.append("\n");

            receipt.append(
                    "Phone Number  : "
            );

            receipt.append(
                    customerPhone
            );

            receipt.append("\n");

            receipt.append(
                    "Movie         : "
            );

            receipt.append(
                    selectedMovie
            );

            receipt.append("\n");

            receipt.append(
                    "Show Time     : "
            );

            receipt.append(
                    selectedShow
            );

            receipt.append("\n");

            receipt.append(
                    "Seats         : "
            );

            receipt.append(
                    String.join(
                            ", ",
                            selectedSeats
                    )
            );

            receipt.append("\n");

            receipt.append(
                    "Total Seats   : "
            );

            receipt.append(
                    selectedSeats.size()
            );

            receipt.append("\n\n");

            receipt.append(
                    "========================================\n"
            );

            receipt.append(
                    "          BOOKING CONFIRMED\n"
            );

            receipt.append(
                    "========================================\n"
            );

            lastReceipt =
                    receipt.toString();

            billArea.setText(
                    lastReceipt
            );

            saveReceiptToFile(
                    lastReceipt
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Booking successful!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearSelectedSeats();

            clearCustomerFields();

            loadBookedSeats(
                    showId
            );

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Booking failed:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // SAVE RECEIPT TO FILE
    // =========================================================

    private void saveReceiptToFile(
            String receipt
    ) {

        try (
                FileWriter writer =
                        new FileWriter(
                                "receipt.txt",
                                true
                        )
        ) {

            writer.write(receipt);

            writer.write(
                    "\n\n"
            );

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not save receipt:\n"
                            + e.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // DOWNLOAD TICKET
    // =========================================================

    private void downloadTicket() {

        if (
                lastReceipt == null
                        ||
                        lastReceipt.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "No ticket is available to download.",
                    "Download Ticket",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        JFileChooser fileChooser =
                new JFileChooser();

        fileChooser.setDialogTitle(
                "Save Movie Ticket"
        );

        fileChooser.setSelectedFile(
                new File(
                        "Movie_Ticket.txt"
                )
        );

        int result =
                fileChooser.showSaveDialog(
                        this
                );

        if (
                result
                        != JFileChooser.APPROVE_OPTION
        ) {

            return;
        }

        File file =
                fileChooser.getSelectedFile();

        try (
                FileWriter writer =
                        new FileWriter(
                                file
                        )
        ) {

            writer.write(
                    lastReceipt
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Ticket downloaded successfully.",
                    "Download Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not download ticket:\n"
                            + e.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // VIEW BOOKINGS
    // =========================================================

    private void showBookings() {

        String[] columns = {

                "Booking ID",
                "Customer Name",
                "Phone",
                "Movie",
                "Show Time",
                "Seat"
        };

        DefaultTableModel tableModel =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {

                        return false;
                    }
                };

        String sql =
                "SELECT b.booking_id, " +
                        "b.customer_name, " +
                        "b.customer_phone, " +
                        "m.movie_name, " +
                        "s.show_time, " +
                        "b.seat_code " +
                        "FROM bookings b " +
                        "JOIN shows s " +
                        "ON b.show_id = s.show_id " +
                        "JOIN movies m " +
                        "ON s.movie_id = m.movie_id " +
                        "ORDER BY b.booking_id";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        );

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (
                    resultSet.next()
            ) {

                Object[] row = {

                        resultSet.getInt(
                                "booking_id"
                        ),

                        resultSet.getString(
                                "customer_name"
                        ),

                        resultSet.getString(
                                "customer_phone"
                        ),

                        resultSet.getString(
                                "movie_name"
                        ),

                        resultSet.getString(
                                "show_time"
                        ),

                        resultSet.getString(
                                "seat_code"
                        )
                };

                tableModel.addRow(
                        row
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading bookings:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        JTable bookingTable =
                new JTable(
                        tableModel
                );

        bookingTable.setAutoCreateRowSorter(
                true
        );

        bookingTable.setRowHeight(
                28
        );

        bookingTable.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        bookingTable.getTableHeader()
                .setFont(
                        new Font(
                                "SansSerif",
                                Font.BOLD,
                                13
                        )
                );

        bookingTable.getTableHeader()
                .setReorderingAllowed(
                        false
                );

        JScrollPane scrollPane =
                new JScrollPane(
                        bookingTable
                );

        // =====================================================
        // BUTTONS FOR UPDATE / CANCEL
        // =====================================================

        JButton updateButton =
                new JButton(
                        "UPDATE SELECTED"
                );

        JButton cancelButton =
                new JButton(
                        "CANCEL SELECTED"
                );

        stylePrimaryButton(
                updateButton
        );

        styleSecondaryButton(
                cancelButton
        );

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout()
                );

        buttonPanel.add(
                updateButton
        );

        buttonPanel.add(
                cancelButton
        );

        JFrame bookingFrame =
                new JFrame(
                        "All Bookings"
                );

        bookingFrame.setSize(
                950,
                500
        );

        bookingFrame.setLocationRelativeTo(
                this
        );

        bookingFrame.setLayout(
                new BorderLayout(10, 10)
        );

        bookingFrame.add(
                scrollPane,
                BorderLayout.CENTER
        );

        bookingFrame.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        bookingFrame.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        // =====================================================
        // UPDATE
        // =====================================================

        updateButton.addActionListener(
                e -> updateSelectedBooking(
                        bookingTable,
                        tableModel
                )
        );

        // =====================================================
        // CANCEL
        // =====================================================

        cancelButton.addActionListener(
                e -> cancelSelectedBooking(
                        bookingTable,
                        tableModel
                )
        );

        bookingFrame.setVisible(
                true
        );
    }

    // =========================================================
    // UPDATE SELECTED BOOKING
    // =========================================================

    private void updateSelectedBooking(
            JTable bookingTable,
            DefaultTableModel tableModel
    ) {

        int selectedRow =
                bookingTable.getSelectedRow();

        if (
                selectedRow == -1
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a booking first.",
                    "Update Booking",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int modelRow =
                bookingTable.convertRowIndexToModel(
                        selectedRow
                );

        int bookingId =
                Integer.parseInt(
                        tableModel
                                .getValueAt(
                                        modelRow,
                                        0
                                )
                                .toString()
                );

        String oldName =
                tableModel
                        .getValueAt(
                                modelRow,
                                1
                        )
                        .toString();

        String oldPhone =
                tableModel
                        .getValueAt(
                                modelRow,
                                2
                        )
                        .toString();

        JTextField newNameField =
                new JTextField(
                        oldName
                );

        JTextField newPhoneField =
                new JTextField(
                        oldPhone
                );

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                2,
                                2,
                                10,
                                10
                        )
                );

        panel.add(
                new JLabel(
                        "Customer Name:"
                )
        );

        panel.add(
                newNameField
        );

        panel.add(
                new JLabel(
                        "Phone:"
                )
        );

        panel.add(
                newPhoneField
        );

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Update Booking",
                        JOptionPane.OK_CANCEL_OPTION
                );

        if (
                result
                        != JOptionPane.OK_OPTION
        ) {

            return;
        }

        String newName =
                newNameField.getText()
                        .trim();

        String newPhone =
                newPhoneField.getText()
                        .trim();

        if (
                newName.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Customer name cannot be empty.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (
                !newPhone.matches(
                        "\\d{10}"
                )
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Phone number must contain exactly 10 digits.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String sql =
                "UPDATE bookings " +
                        "SET customer_name = ?, " +
                        "customer_phone = ? " +
                        "WHERE booking_id = ?";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            statement.setString(
                    1,
                    newName
            );

            statement.setString(
                    2,
                    newPhone
            );

            statement.setInt(
                    3,
                    bookingId
            );

            int rows =
                    statement.executeUpdate();

            if (
                    rows == 1
            ) {

                tableModel.setValueAt(
                        newName,
                        modelRow,
                        1
                );

                tableModel.setValueAt(
                        newPhone,
                        modelRow,
                        2
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Booking updated successfully.",
                        "Update Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Booking could not be updated.",
                        "Update Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Update failed:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // CANCEL SELECTED BOOKING
    // =========================================================

    private void cancelSelectedBooking(
            JTable bookingTable,
            DefaultTableModel tableModel
    ) {

        int selectedRow =
                bookingTable.getSelectedRow();

        if (
                selectedRow == -1
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a booking first.",
                    "Cancel Booking",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int modelRow =
                bookingTable.convertRowIndexToModel(
                        selectedRow
                );

        int bookingId =
                Integer.parseInt(
                        tableModel
                                .getValueAt(
                                        modelRow,
                                        0
                                )
                                .toString()
                );

        String customerName =
                tableModel
                        .getValueAt(
                                modelRow,
                                1
                        )
                        .toString();

        String movieName =
                tableModel
                        .getValueAt(
                                modelRow,
                                3
                        )
                        .toString();

        String showTime =
                tableModel
                        .getValueAt(
                                modelRow,
                                4
                        )
                        .toString();

        String seatCode =
                tableModel
                        .getValueAt(
                                modelRow,
                                5
                        )
                        .toString();

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to cancel this booking?\n\n"
                                + "Booking ID : "
                                + bookingId
                                + "\n"
                                + "Customer   : "
                                + customerName
                                + "\n"
                                + "Movie      : "
                                + movieName
                                + "\n"
                                + "Show Time  : "
                                + showTime
                                + "\n"
                                + "Seat       : "
                                + seatCode,
                        "Confirm Cancellation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (
                choice
                        != JOptionPane.YES_OPTION
        ) {

            return;
        }

        String deleteSql =
                "DELETE FROM bookings " +
                        "WHERE booking_id = ?";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                deleteSql
                        )
        ) {

            statement.setInt(
                    1,
                    bookingId
            );

            int rowsAffected =
                    statement.executeUpdate();

            if (
                    rowsAffected == 1
            ) {

                tableModel.removeRow(
                        modelRow
                );

                refreshCurrentSeats();

                JOptionPane.showMessageDialog(
                        this,
                        "Booking cancelled successfully.",
                        "Cancellation Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Booking could not be cancelled.",
                        "Cancellation Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cancellation failed:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // REFRESH CURRENT SHOW SEATS
    // =========================================================

    private void refreshCurrentSeats() {

        String selectedShow =
                (String)
                        showComboBox
                                .getSelectedItem();

        if (
                selectedShow == null
        ) {

            return;
        }

        Integer showId =
                showMap.get(
                        selectedShow
                );

        if (
                showId != null
        ) {

            loadBookedSeats(
                    showId
            );
        }
    }

    // =========================================================
    // CLEAR SELECTED SEATS
    // =========================================================

    private void clearSelectedSeats() {

        selectedSeats.clear();

        for (
                String seatName :
                seatButtonMap.keySet()
        ) {

            JToggleButton button =
                    seatButtonMap.get(
                            seatName
                    );

            if (
                    button.isEnabled()
            ) {

                button.setSelected(false);

                button.setText(
                        seatName
                );

                button.setBackground(
                        Color.WHITE
                );

                button.setForeground(
                        new Color(
                                35,
                                40,
                                50
                        )
                );
            }
        }
    }

    // =========================================================
    // CLEAR CUSTOMER FIELDS
    // =========================================================

    private void clearCustomerFields() {

        nameField.setText("");

        phoneField.setText("");
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    MainFrame frame =
                            new MainFrame();

                    frame.setVisible(
                            true
                    );
                }
        );
    }
}