package mummymaze;

public class Enemy {
    protected int line;
    protected int column;
    protected char estado;

    public Enemy(int line, int column, char estado) {
        this.line = line;
        this.column = column;
        this.estado = estado;
    }

    public int getColumn() {
        return column;
    }

    public int getLine() {
        return line;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }
}
