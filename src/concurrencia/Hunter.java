package concurrencia;

import static java.lang.Thread.sleep;
import java.util.Random;

public class Hunter extends Thread implements FieldItem {

    private final HuntField field;
    private Position position;
    private int acurateShots;
    private boolean isAlive = true;

    public Hunter(HuntField field) {
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
        return false;
    }

    @Override
    public char getType() {
        return 'H';
    }

    @Override
    public void run() {
        Position firePosition;
        int direction = new Random().nextInt(4);
        if (direction == 0) {
            firePosition = new Position(position.getX() + 1, position.getY());
        } else if (direction == 1) {
            firePosition = new Position(position.getX(), position.getY() + 1);;
        } else if (direction == 2) {
            firePosition = new Position(position.getX() - 1, position.getY());
        } else {
            firePosition = new Position(position.getX(), position.getY() - 1);
        }
        while (isAlive) {
            try {
                sleep(new Random().nextInt(1)*100);
            } catch (InterruptedException ex) {
            }
            if (field.shot(firePosition)) {
                acurateShots++;
                field.moveItem(this, position, firePosition);
                position = firePosition;
            }
            direction++;
            if (direction == 0) {
                firePosition = new Position(position.getX() + 1, position.getY());
            } else if (direction == 1) {
                firePosition = new Position(position.getX(), position.getY() - 1);;
            } else if (direction == 2) {
                firePosition = new Position(position.getX() - 1, position.getY());
            } else {
                firePosition = new Position(position.getX(), position.getY() + 1);
                direction = -1;
            }
        }
    }
}