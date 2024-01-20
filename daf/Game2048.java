import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;
import java.awt.geom.*;
 
public class Game2048 extends JPanel {
	 
	Rectangle2D rect = new Rectangle(725, 445, 140, 60);
    
    enum State {
        start, won, running, over
    }
 
    final Color[] colorTable = {
        new Color(0x701710), new Color(0xFFE4C3), new Color(0xfff4d3),
        new Color(0xffdac3), new Color(0xe7b08e), new Color(0xe7bf8e),
        new Color(0xffc4c3), new Color(0xE7948e), new Color(0xbe7e56),
        new Color(0xbe5e56), new Color(0x9c3931), new Color(0x701710)};
 
    final static int target = 2048;
    
    static int highest;
    static int score;
    static int max;
    static int now;
    int HighestScore;
    int count = 0;
 
    private Color gridColor = new Color(0xBBADA0);
    private Color emptyColor = new Color(0xCDC1B4);
    private Color startColor = new Color(0xFFEBCD);
 
    private Random rand = new Random();
 
    private Tile[][] tiles;
    private int side = 4;
    private State gamestate = State.start;
    private boolean checkingAvailableMoves;
 
    public Game2048() {
        setPreferredSize(new Dimension(900, 700));
        setBackground(new Color(0xFAF8EF));
        setFont(new Font("SansSerif", Font.BOLD, 48)); //Sans Serif 폰트 사용
        setFocusable(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startGame(); // 게임 시작
                repaint();
            }
        });
 
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        moveUp();
                        break;//윗 방향키
                    case KeyEvent.VK_DOWN:
                        moveDown();
                        break;//아랫 방향키
                    case KeyEvent.VK_LEFT:
                        moveLeft();
                        break;//왼쪽 방향키
                    case KeyEvent.VK_RIGHT:
                        moveRight();
                        break;//오른쪽 방향키
                }
                repaint();
            }
        });
    }
 
    @Override
    public void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        Graphics2D g = (Graphics2D) gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON); // 안티알리어싱 사용 // 도형의 외곽 모형을 다듬는? api
 
        drawGrid(g);
    }
 
    void startGame() {
        if (gamestate != State.running) {
            count++;
        	score = 0;
            highest = 0;
            max = 0;
            now = 0;
            gamestate = State.running;
            tiles = new Tile[side][side];
            first();
            first(); // 랜덤한 위치에 타일 생성
        }
    }
 
    void drawGrid(Graphics2D g) {
        g.setColor(gridColor);
        g.fillRoundRect(200, 100, 499, 499, 15, 15);//가장자리 그리드
 
        if (gamestate == State.running) {
            for (int r = 0; r < side; r++) {
                for (int c = 0; c < side; c++) {
                    if (tiles[r][c] == null) {
                        g.setColor(emptyColor);
                        g.fillRoundRect(215 + c * 121, 115 + r * 121, 106, 106, 7, 7);
                    } else {
                        drawTile(g, r, c);
                    }
                }
            }
            g.setFont(new Font("SansSerif", Font.BOLD, 30));
            
            g.setColor(gridColor);
            g.fillRoundRect(710, 100, 170, 120, 8, 8);
            g.setColor(startColor);
            g.fillRoundRect(725, 145, 140, 60, 7, 7);
            g.setColor(gridColor.darker());
            g.drawString("Top", 765 , 130);
            g.drawString("" + max , 755 , 185);
            
            g.setColor(gridColor);
            g.fillRoundRect(710, 250, 170, 120, 8, 8);
            g.setColor(startColor);
            g.fillRoundRect(725, 295, 140, 60, 7, 7);
            g.setColor(gridColor.darker());
            g.drawString("Score", 750 , 280);
            g.drawString("" + now , 755 , 335);
 
            g.setColor(startColor);
            g.fill(rect);
            g.setColor(gridColor.darker());
            g.drawString("끝내기", 745 , 485);
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                	 if ((e.getButton() == 1) && rect.contains(e.getX(), e.getY()) ) {
                		 gamestate = State.over;
                		 int num = 1;
                		 for(int i = 0; i < 4; i++)
                		 {
                			 for(int j = 0; j< 4; j++)
                			 {
                				 tiles[i][j] = new Tile(num);
                				 num++;
                			 }
                		 }
	 
                	   }
                }
            });

        } else { // 처음 시작 화면
            g.setColor(startColor);
            g.fillRoundRect(215, 115, 469, 469, 7, 7);
 
            g.setColor(gridColor.darker());
            g.setFont(new Font("SansSerif", Font.BOLD, 128));
            g.drawString("2048", 310, 270);
 
            g.setFont(new Font("SansSerif", Font.BOLD, 30));
 
            if (gamestate == State.won) {
                g.drawString("you made it!", 390, 350);
 
            } 
            else if (gamestate == State.over) {
            	
            	if(now >= HighestScore)
                {
                	HighestScore = now;
                }
      
            	g.drawString("game over", 390, 330);
            	g.drawString("Highest Score : " + HighestScore , 330 , 380);
                g.drawString("Score : " + now , 380 , 430);
                g.setFont(new Font("SansSerif", Font.BOLD, 25));
                g.setColor(gridColor);
                g.drawString("새게임을 시작하려면 클릭하세요", 270, 490);
                
            }
            
 
            g.setColor(gridColor);
            if(count == 0)
            {
            	g.drawString("시작하려면 클릭하세요", 290, 490);
            }
            // 메인 타이틀 글씨
        }
    }
 
    void drawTile(Graphics2D g, int r, int c) {
        int value = tiles[r][c].getValue();
 
        g.setColor(colorTable[(int) (Math.log(value) / Math.log(2)) + 1]);//타일 색상 설정
        g.fillRoundRect(215 + c * 121, 115 + r * 121, 106, 106, 7, 7);
        String s = String.valueOf(value);
 
        g.setColor(value < 128 ? colorTable[0] : colorTable[1]);//타일 숫자 색깔 설정
 
        FontMetrics fm = g.getFontMetrics();
        int asc = fm.getAscent();
        int dec = fm.getDescent();
 
        int x = 215 + c * 121 + (106 - fm.stringWidth(s)) / 2;
        int y = 115 + r * 121 + (asc + (106 - (asc + dec)) / 2);
 
        g.drawString(s, x, y);//타일 숫자 생성
    }
 
 
    private void first() {
        int pos = rand.nextInt(side * side);
        int row, col;
        do {
            pos = (pos + 1) % (side * side);
            row = pos / side;
            col = pos % side;
        } while (tiles[row][col] != null);
 
        int val = 2;
        tiles[row][col] = new Tile(val);
    }
    
    private void addRandomTile() {
        int pos = rand.nextInt(side * side);
        int row, col;
        do {
            pos = (pos + 1) % (side * side);
            row = pos / side;
            col = pos % side;
        } while (tiles[row][col] != null);
 
        int val = 2;
        int fo = 4;
        int per = (int) (Math.random() * 100) + 1;
        
        if(1<=per && per <= 90) {
    		tiles[row][col] = new Tile(val);
    	}
    	else {
    		tiles[row][col] = new Tile(fo);
    	}
    }
 
    private boolean move(int countDownFrom, int yIncr, int xIncr) {
        boolean moved = false;
 
        for (int i = 0; i < side * side; i++) {
            int j = Math.abs(countDownFrom - i);
 
            int r = j / side;
            int c = j % side;
 
            if (tiles[r][c] == null)
                continue;
 
            int nextR = r + yIncr;
            int nextC = c + xIncr;
 
            while (nextR >= 0 && nextR < side && nextC >= 0 && nextC < side) {
 
                Tile next = tiles[nextR][nextC];
                Tile curr = tiles[r][c];
 
                if (next == null) {
 
                    if (checkingAvailableMoves)
                        return true;
                    
                    tiles[nextR][nextC] = curr;
                    tiles[r][c] = null;
                    r = nextR;
                    c = nextC;
                    nextR += yIncr;
                    nextC += xIncr;
                    moved = true;
 
                } else if (next.canMergeWith(curr)) {
 
                    if (checkingAvailableMoves)
                        return true;
 
                    int value = next.mergeWith(curr);
                    if (value > highest)
                        highest = value;
                    score += value;
                    now += value;
                    if(max<highest) {
                    	max = value; 	
                    }
                    tiles[r][c] = null;
                    moved = true;
                    break;
                } else
                    break;
            }
        }
 
        if (moved) {
            if (highest < target) {
                clearMerged();
                addRandomTile();
                if (!movesAvailable()) {
                    gamestate = State.over;
                }
            } else if (highest == target)
                gamestate = State.won;
        }
 
        return moved;
    }
 
    boolean moveUp() {
        return move(0, -1, 0);
    }
 
    boolean moveDown() {
        return move(side * side - 1, 1, 0);
    }
 
    boolean moveLeft() {
        return move(0, 0, -1);
    }
 
    boolean moveRight() {
        return move(side * side - 1, 0, 1);
    }
 
    void clearMerged() {
        for (Tile[] row : tiles)
            for (Tile tile : row)
                if (tile != null)
                    tile.setMerged(false);
    }
 
    boolean movesAvailable() {
        checkingAvailableMoves = true;
        boolean hasMoves = moveUp() || moveDown() || moveLeft() || moveRight();
        checkingAvailableMoves = false;
        return hasMoves;
    }
 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setTitle("2048");
            f.setResizable(true);
            f.add(new Game2048(), BorderLayout.CENTER);
            f.pack();
            f.setLocationRelativeTo(null);
            
            f.setVisible(true);
        });
    }
}