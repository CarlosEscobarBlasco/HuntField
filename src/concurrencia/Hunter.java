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
        System.out.println("mori");
        field.removeItem(this, position);
        return false;
    }

    @Override
    public char getType() {
        return 'H';
    }

    private Position calculatePosition(int number) {
        switch (number) {
            case 0:
                return new Position(position.getX() + 1, position.getY());
            case 1:
                return new Position(position.getX(), position.getY() + 1);
            case 2:
                return new Position(position.getX() - 1, position.getY());
            case 3:
                return new Position(position.getX(), position.getY() - 1);
        }
        return null;
    }

    @Override
    public void run() {

        int direction = new Random().nextInt(4);
        Position firePosition = calculatePosition(direction);
        while (isAlive) {
            try {
                sleep(new Random().nextInt(1) * 100);
            } catch (InterruptedException ex) {
            }
            if (field.shot(firePosition)) {
                acurateShots++;
                field.moveItem(this, position, firePosition);
                position = firePosition;
            }
            direction = (direction < 3) ? direction + 1 : 0;
            firePosition = calculatePosition(direction);
        }
    }
}