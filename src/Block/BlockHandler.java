package Block;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;

/**
 * Zarządza blokami mapy i wejściami do mini-gier.
 */
public class BlockHandler {
    GamePanel gp;
    Block[] mapBlocks;

    public BlockHandler(GamePanel gp) {
        this.gp = gp;
        mapBlocks = new Block[10];
    }

    /**
     * Wczytuje obrazy bloków.
     */
    public void blockImage() {
        try {
            mapBlocks[0] = new Block();
            mapBlocks[0].image = ImageIO.read(getClass().getResourceAsStream("/Images/floor.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Rysuje mapę i wejścia do mini-gier.
     * @param g2 obiekt Graphics2D
     */
    public void draw(Graphics2D g2) {
        int column = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        // Rysuj podłogę
        while (column < gp.maxCol && row < gp.maxRow) {
            g2.drawImage(mapBlocks[0].image, x, y, gp.tileSize, gp.tileSize, null);
            column++;
            x += gp.tileSize;

            if (column == gp.maxCol) {
                column = 0;
                x = 0;
                row++;
                y += gp.tileSize;
            }
        }

        // Wejście do GRY 1
        g2.setColor(Color.BLACK);
        g2.fillRect(0, gp.tileSize * 3, gp.tileSize, gp.tileSize);

        if (gp.getEnterMiniGame1() == 1) {
            g2.setColor(Color.RED);
            g2.fillRect(0, gp.tileSize * 3, gp.tileSize, gp.tileSize);
        }

        // Wejście do GRY 2
        g2.setColor(Color.BLACK);
        g2.fillRect(gp.tileSize * 10, gp.tileSize * 11, gp.tileSize, gp.tileSize);

        if (gp.getEnterMiniGame2() == 1) {
            g2.setColor(Color.RED);
            g2.fillRect(gp.tileSize * 10, gp.tileSize * 11, gp.tileSize, gp.tileSize);
        }

        // Wejście do GRY 3
        g2.setColor(Color.BLACK);
        g2.fillRect(gp.tileSize * 15, gp.tileSize * 5, gp.tileSize, gp.tileSize);

        if (gp.getEnterMiniGame3() == 1) {
            g2.setColor(Color.RED);
            g2.fillRect(gp.tileSize * 15, gp.tileSize * 5, gp.tileSize, gp.tileSize);
        }
    }
}