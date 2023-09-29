import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Game {

    public static final int a = 100;

    private Boolean b;

    private int c;
    private int d;
    private int e;

    private Bird f;
    private ArrayList<Pipe> g;
    private Keyboard h;

    public int i;
    public Boolean j;
    public Boolean k;

    public Game() {
        h = Keyboard.getInstance();
        restart();
    }

    public void restart() {
        b = false;
        k = false;
        j = false;

        i = 0;
        c = 0;
        d = 0;
        e = 0;

        f = new Bird();
        g = new ArrayList<Pipe>();
    }

    public void update() {
        lookForForStart();

        if (!k)
            return;

        lookForForPause();
        lookForForReset();

        if (b)
            return;

        f.update();

        if (j)
            return;

        movePipes();
        checkForCollisions();
    }

    public ArrayList<Render> getRenders() {
        ArrayList<Render> renders = new ArrayList<Render>();
        renders.add(new Render(0, 0, "lib/background.png"));
        for (Pipe pipe : g)
            renders.add(pipe.getRender());
        renders.add(new Render(0, 0, "lib/foreground.png"));
        renders.add(f.getRender());
        return renders;
    }

    private void lookForForStart() {
        if (!k && h.isDown(KeyEvent.VK_SPACE)) {
            k = true;
        }
    }

    private void lookForForPause() {
        if (c > 0)
            c--;

        if (h.isDown(KeyEvent.VK_P) && c <= 0) {
            b = !b;
            c = 10;
        }
    }

    private void lookForForReset() {
        if (d > 0)
            d--;

        if (h.isDown(KeyEvent.VK_R) && d <= 0) {
            restart();
            d = 10;
            return;
        }
    }

    private void movePipes() {
        e--;

        if (e < 0) {
            e = a;
            Pipe northPipe = null;
            Pipe southPipe = null;

            // Look for g off the screen
            for (Pipe pipe : g) {
                if (pipe.x - pipe.width < 0) {
                    if (northPipe == null) {
                        northPipe = pipe;
                    } else if (southPipe == null) {
                        southPipe = pipe;
                        break;
                    }
                }
            }

            if (northPipe == null) {
                Pipe pipe = new Pipe("north");
                g.add(pipe);
                northPipe = pipe;
            } else {
                northPipe.reset();
            }

            if (southPipe == null) {
                Pipe pipe = new Pipe("south");
                g.add(pipe);
                southPipe = pipe;
            } else {
                southPipe.reset();
            }

            northPipe.y = southPipe.y + southPipe.height + 175;
        }

        for (Pipe pipe : g) {
            pipe.update();
        }
    }

    private void checkForCollisions() {

        for (Pipe pipe : g) {
            if (pipe.collides(f.x, f.y, f.width, f.height)) {
                j = true;
                f.dead = true;
            } else if (pipe.x == f.x && pipe.orientation.equalsIgnoreCase("south")) {
                i++;
            }
        }

        // Ground + Bird collision
        if (f.y + f.height > App.HEIGHT - 80) {
            j = true;
            f.y = App.HEIGHT - 80 - f.height;
        }
    }
}