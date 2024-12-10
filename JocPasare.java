import javax.swing.*;              
import java.awt.*;              
import java.awt.event.ActionEvent;              
import java.awt.event.ActionListener;              
import java.awt.event.KeyAdapter;              
import java.awt.event.KeyEvent;              
import java.util.ArrayList;              
import java.util.List;              
import java.util.Random;              
import javax.imageio.ImageIO;              
import java.io.File;              
import java.io.IOException;              
import javax.sound.sampled.*;              

public class JocPasare extends JPanel implements ActionListener {              
    private static final int LATIME = 800, INALTIME = 600;              
    private static final int LATIME_TEPAVA = 50;              
    private static final int SPATIU_TEPAVA = 200;              
    private static final int MARIME_PASARE = 70;              
    private int pozitiePasare = INALTIME / 2, vitezaPasare = 0;              
    private List<Rectangle> tevi;              
    private List<Integer> teviDy; // Vertical speed for moving pipes              
    private Timer cronometru;              
    private boolean jocTerminat = false;              
    private boolean jocPornit = false;              
    private int scor = 0;              
    private int nivel = 1; // Track the current level              
    private Image imaginePasare;              
    private Image imagineFundal;              
    private Image texturaCopac;              
    private Random random;              
    private Clip clip;              
    private int vitezaTevi = 5;              
    private int distantaTevi = 200;              
    private JButton butonStart;              

    public JocPasare() {              
        setPreferredSize(new Dimension(LATIME, INALTIME));              
        tevi = new ArrayList<>();              
        teviDy = new ArrayList<>(); // Initialize vertical speed list              
        cronometru = new Timer(20, this);              
        random = new Random();              

        // Key listener for game controls              
        addKeyListener(new KeyAdapter() {              
            @Override              
            public void keyPressed(KeyEvent e) {              
                if (e.getKeyCode() == KeyEvent.VK_SPACE && !jocPornit && !jocTerminat) {              
                    startJoc();              
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE && jocPornit && !jocTerminat) {              
                    vitezaPasare = -10;              
                    playSound("C:/Users/Administrator/Desktop/java/HappyShrek-Calinescu Marius Catalin/src/jump.wav");              
                } else if (e.getKeyCode() == KeyEvent.VK_R && jocTerminat) {              
                    reseteazaJoc();              
                }              
            }              
        });              
        setFocusable(true);              
        requestFocusInWindow();              

        // Create and configure the new Start button              
        butonStart = new JButton("START");              
        butonStart.setBounds(LATIME / 2 - 75, INALTIME / 2 - 25, 150, 50);              
        butonStart.setFont(new Font("Arial", Font.BOLD, 20));              
        butonStart.setBackground(Color.WHITE);  // Set button color to white              
        butonStart.setForeground(Color.BLACK);  // Set text color to black              
        butonStart.setFocusPainted(false);              
        butonStart.addActionListener(new ActionListener() {              
            @Override              
            public void actionPerformed(ActionEvent e) {              
                startJoc();              
            }              
        });              
        this.setLayout(null);              
        this.add(butonStart); // Add the start button              

        // Load images for background, bird, and trees              
        loadImages();              
    }              

    private void loadImages() {              
        try {              
            imaginePasare = ImageIO.read(new File("C:/Users/Administrator/Desktop/java/HappyShrek-Calinescu Marius Catalin/src/shrek.png"));              
            imagineFundal = ImageIO.read(new File("C:/Users/Administrator/Desktop/java/HappyShrek-Calinescu Marius Catalin/src/fundal.jpg"));              
            texturaCopac = ImageIO.read(new File("C:/Users/Administrator/Pictures/tree_texture.jpg"));              
        } catch (IOException e) {              
            e.printStackTrace();              
            System.out.println("Eroare la încărcarea imaginilor.");              
        }              
    }              

    private void playSound(String soundFilePath) {              
        try {              
            if (clip != null && clip.isRunning()) {              
                clip.stop();              
            }              
            File soundFile = new File(soundFilePath);              
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);              
            clip = AudioSystem.getClip();              
            clip.open(audioInputStream);              
            clip.start();              
        } catch (Exception e) {              
            e.printStackTrace();              
            System.out.println("Eroare la redarea sunetului.");              
        }              
    }              

    private void genereazaTevi() {              
        tevi.clear();              
        teviDy.clear(); // Clear vertical speed list              
        int x = LATIME;              
        for (int i = 0; i < 5; i++) {              
            int inaltimeTeava = 100 + random.nextInt(200);              
            tevi.add(new Rectangle(x + i * distantaTevi, INALTIME - inaltimeTeava, LATIME_TEPAVA, inaltimeTeava));              
            tevi.add(new Rectangle(x + i * distantaTevi, 0, LATIME_TEPAVA, INALTIME - inaltimeTeava - SPATIU_TEPAVA));              
            teviDy.add(0); // Initially, pipes are not moving vertically              
            teviDy.add(0);              
        }              
    }              

    private void reseteazaJoc() {              
        pozitiePasare = INALTIME / 2;              
        vitezaPasare = 0;              
        scor = 0;              
        nivel = 1; // Reset level              
        jocTerminat = false;              
        vitezaTevi = 5;              
        distantaTevi = 200;              
        genereazaTevi();              
        jocPornit = false;              
        butonStart.setVisible(true); // Show the Start button to start the game              
    }              

    private void startJoc() {              
        jocPornit = true;              
        cronometru.start();              
        butonStart.setVisible(false); // Hide the Start button              
        genereazaTevi();              
        repaint();              
    }              

    private void updateDifficulty() {          
        // Increase difficulty based on score          
        if (scor < 10) {          
            vitezaTevi = 5; // Easy          
            distantaTevi = 200;          
        } else if (scor < 20) {          
            vitezaTevi = 7; // Medium          
            distantaTevi = 180;          
        } else if (scor < 50) {          
            vitezaTevi = 9; // Hard          
            distantaTevi = 160;          
        } else if (scor < 100) {          
            nivel = 2; // Introduce moving obstacles          
            vitezaTevi = 10;          
            distantaTevi = 150;          
            for (int i = 0; i < teviDy.size(); i++) {          
                teviDy.set(i, random.nextInt(3) + 1); // Random vertical speed for pipes          
            }          
        } else if (scor < 150) {          
            nivel = 3; // Faster pipes and smaller gaps          
            vitezaTevi = 12;          
            distantaTevi = 130;          
        } else {          
            nivel = 4; // Maximum difficulty          
            vitezaTevi = 15;          
            distantaTevi = 100;          
        }          
    }          

    @Override              
    public void actionPerformed(ActionEvent e) {              
        if (jocPornit && !jocTerminat) {              
            pozitiePasare += vitezaPasare;              
            vitezaPasare += 1;              

            for (int i = 0; i < tevi.size(); i++) {              
                Rectangle teava = tevi.get(i);              
                teava.x -= vitezaTevi;              

                // Move pipes vertically if level >= 2              
                if (nivel >= 2) {              
                    teava.y += teviDy.get(i);              
                    if (teava.y < 0 || teava.y + teava.height > INALTIME) {              
                        teviDy.set(i, -teviDy.get(i)); // Reverse direction if out of bounds              
                    }              
                }              
            }              

            if (tevi.get(0).x + LATIME_TEPAVA < 0) {              
                tevi.remove(0);              
                teviDy.remove(0);              
                tevi.remove(0);              
                teviDy.remove(0);              
                int x = tevi.get(tevi.size() - 1).x + distantaTevi;              
                int inaltimeTeava = 100 + random.nextInt(200);              
                tevi.add(new Rectangle(x, INALTIME - inaltimeTeava, LATIME_TEPAVA, inaltimeTeava));              
                tevi.add(new Rectangle(x, 0, LATIME_TEPAVA, INALTIME - inaltimeTeava - SPATIU_TEPAVA));              
                teviDy.add(random.nextInt(3) + 1); // Add vertical speed for new pipes              
                teviDy.add(random.nextInt(3) + 1);              
                scor++;              
                updateDifficulty(); // Update difficulty based on score          
            }              

            for (Rectangle teava : tevi) {              
                if (teava.intersects(new Rectangle(LATIME / 2 - MARIME_PASARE / 2, pozitiePasare, MARIME_PASARE, MARIME_PASARE))) {              
                    jocTerminat = true;              
                    playSound("C:/Users/Administrator/Desktop/java/HappyShrek-Calinescu Marius Catalin/src/gameover.wav"); // Play Game Over sound here              
                }              
            }              

            if (pozitiePasare > INALTIME || pozitiePasare < 0) {              
                jocTerminat = true;              
                playSound("C:/Users/Administrator/Desktop/java/HappyShrek-Calinescu Marius Catalin/src/gameover.wav"); // Play Game Over sound here              
            }              
        }              
        repaint();              
    }              

    @Override              
    protected void paintComponent(Graphics g) {              
        super.paintComponent(g);              

        if (!jocPornit) {              
            if (imagineFundal != null) {              
                g.drawImage(imagineFundal, 0, 0, LATIME, INALTIME, this);              
            }              
        } else {              
            if (imagineFundal != null) {              
                g.drawImage(imagineFundal, 0, 0, LATIME, INALTIME, this);              
            }              

            if (imaginePasare != null) {              
                g.drawImage(imaginePasare, LATIME / 2 - MARIME_PASARE / 2, pozitiePasare, MARIME_PASARE, MARIME_PASARE, this);              
            }              

            for (Rectangle teava : tevi) {              
                if (texturaCopac != null) {              
                    g.drawImage(texturaCopac, teava.x, teava.y, teava.width, teava.height, this);              
                } else {              
                    g.setColor(new Color(139, 69, 19));              
                    g.fillRect(teava.x, teava.y, teava.width, teava.height);              
                }              
            }              

            g.setColor(Color.BLACK);              
            g.setFont(new Font("Arial", Font.BOLD, 20));              
            g.drawString("Scor: " + scor, 10, 20);              
            g.drawString("Nivel: " + nivel, 10, 50); // Display the current level              

            if (jocTerminat) {              
                Graphics2D g2d = (Graphics2D) g;              
                g2d.setColor(new Color(255, 255, 255, 200));              
                int rectWidth = 400;              
                int rectHeight = 150;              
                int rectX = LATIME / 2 - rectWidth / 2;              
                int rectY = INALTIME / 2 - rectHeight / 2;              
                g2d.fillRoundRect(rectX, rectY, rectWidth, rectHeight, 20, 20);              

                g.setColor(Color.BLACK);              
                g.setFont(new Font("Arial", Font.BOLD, 40));              
                g.drawString("Joc Terminat", LATIME / 2 - 120, INALTIME / 2 - 20);              
                g.setFont(new Font("Arial", Font.PLAIN, 20));              
                g.drawString("Scor final: " + scor, LATIME / 2 - 80, INALTIME / 2 + 20);              
                g.drawString("Apasă 'R' pentru a restarta", LATIME / 2 - 140, INALTIME / 2 + 60);              
            }              
        }              
    }              

    public static void main(String[] args) {              
        JFrame fereastra = new JFrame("Joc Pasare");              
        JocPasare joc = new JocPasare();              
        fereastra.add(joc);              
        fereastra.pack();              
        fereastra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);              
        fereastra.setLocationRelativeTo(null);              
        fereastra.setVisible(true);              
    }              
}    