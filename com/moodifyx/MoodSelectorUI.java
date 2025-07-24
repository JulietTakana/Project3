package com.moodifyx;

import javax.swing.*;
import java.awt.Image; 
import java.awt.Font;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class MoodSelectorUI extends JFrame {
    private final MoodRepository repository = new MoodRepository();
    private JLabel moodImageLabel;   
    private JLabel songTitleLabel;   

    public MoodSelectorUI() {
        setTitle("Songs for your Mood");
        setSize(600, 500); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setupUI();
        setVisible(true);
    }

    private void setupUI() {
        Set<String> moods = repository.getAllMoods();
        JComboBox<String> moodDropdown = new JComboBox<>(moods.toArray(new String[0]));
        JButton detectMoodBtn = new JButton("Recommend Music");

       
        moodImageLabel = new JLabel();
        moodImageLabel.setHorizontalAlignment(JLabel.CENTER);
        moodImageLabel.setVerticalAlignment(JLabel.CENTER);

       
        songTitleLabel = new JLabel("No song playing");
        songTitleLabel.setHorizontalAlignment(JLabel.CENTER);

        //Updating the image based on selected mood
        moodDropdown.addActionListener(e -> {
            String selectedMood = (String) moodDropdown.getSelectedItem();
            updateMoodImage(selectedMood);
        });

       
        detectMoodBtn.addActionListener(e -> {
            String mood = (String) moodDropdown.getSelectedItem();
            while (true) {
                List<Song> songs = repository.getSongsForMood(mood);
                Song selected = songs.get(ThreadLocalRandom.current().nextInt(songs.size()));
                File file = new File(selected.getFilePath());
                if (file.exists()) {
                    System.out.println("Playing: " + selected.getFilePath());
                    SongPlayer.play(selected.getFilePath());
                    
                    songTitleLabel.setText("Now playing: " + selected.getTitle());

                } else {
                    System.out.println("File not found");
                    songTitleLabel.setText("File not found for: " + selected.getTitle());
                    break;
                }

                Object[] options = {"Another song", "Change Mood", "Stop Music and Exit"};
                int choice = JOptionPane.showOptionDialog(
                        this,
                        "Now playing: "  + selected.getTitle() + "\nMood: " + mood,
                        "Your Mood Music",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                );
                SongPlayer.stop();
                if (choice == JOptionPane.YES_OPTION) {
                    continue;
                } else if (choice == JOptionPane.NO_OPTION) {
                    break;
                } else {
                    System.exit(0);
                }
            }
        });

        
        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Select Your Mood: "));
        controlPanel.add(moodDropdown);
        controlPanel.add(detectMoodBtn);

        
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(controlPanel);
        add(songTitleLabel);  
        System.out.println("\n");
        add(moodImageLabel);  

        
        if (!moods.isEmpty()) {
            updateMoodImage(moods.iterator().next());
        }
    }

private void updateMoodImage(String mood) {
    String imagePath = repository.getImageForMood(mood);
    File imgFile = new File(imagePath);
    if (imgFile.exists()) {
        ImageIcon rawIcon = new ImageIcon(imagePath);
        int desiredWidth = 1000;
        int desiredHeight = 800;
        Image scaled = rawIcon.getImage().getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);
        moodImageLabel.setIcon(new ImageIcon(scaled));
        moodImageLabel.setText("");
    } else {
        moodImageLabel.setIcon(null);
        moodImageLabel.setText("No image available for " + mood);
    }
}



}

