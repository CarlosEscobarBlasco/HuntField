package concurrencia;

import static java.lang.Thread.*;
import java.util.Random;

public class Duck extends Thread implements FieldItem {

    private final HuntField field;
    private Position position;
    private boolean isAlive = true;

    public Duck(HuntField field) {
        boolean searchPosition = true;
        this.field = field;
        while (searchPosition) {
            position = new Position(new Random().nextInt(field.getXLength()), new Random().nextInt(field.getYLength()));
            if (field.setItem(this, position)) {
                searchPosition = false;
            }
        }
    }

    @Override
    public boolean fired() {
        isAlive = false;
        field.removeItem(this, position);
        return true;
    }

    @Override
    public char getType() {
        return 'D';
    }

    @Override
    public void run() {
        while (isAlive) {
            Position toPosition;
            int direction = new Random().nextInt(4);
            if (direction == 0) {
                toPosition = new Position(position.getX() + 1, position.getY());
            } else if (direction == 1) {
                toPosition = new Position(position.getX(), position.getY() + 1);
            } else if (direction == 2) {
                toPosition = new Position(position.getX() - 1, position.getY());
            } else {
                toPosition = new Position(position.getX(), position.getY() - 1);
            }
            try {
                sleep(new Random().nextInt(3)*100);
            } catch (InterruptedException ex) {
            }
            if (field.moveItem(this, position, toPosition)) {
                position = toPosition;

            }
        }
    }
}