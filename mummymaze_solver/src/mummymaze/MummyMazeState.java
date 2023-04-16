package mummymaze;

import agent.Action;
import agent.State;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class MummyMazeState extends State implements Cloneable {

    private final char[][] matrix;
    private int heroLine;
    private int heroColumn;
    private int exitLine;
    private int exitColumn;
    private int keyLine;
    private int keyColumn;
    private LinkedList<Enemy> whiteMummys = new LinkedList<Enemy>();
    private int whiteMummyLine;
    private int whiteMummyColumn;
    private LinkedList<Enemy> redMummys = new LinkedList<Enemy>();
    private int redMummyLine;
    private int redMummyColumn;
    private LinkedList<Enemy> scorpions = new LinkedList<Enemy>();
    private int scorpionLine;
    private int scorpionColumn;
    private LinkedList<Coordenadas> doors = new LinkedList<Coordenadas>();
    private int doorLine;
    private int doorColumn;
    private LinkedList<Coordenadas> traps = new LinkedList<Coordenadas>();
    private int trapLine;
    private int trapColumn;
    private boolean morreu;


    public MummyMazeState(char[][] matrix,int  keyLine,int keyColumn,int exitLine,int exitColumn,LinkedList<Coordenadas> traps, LinkedList<Coordenadas> doors) {
        this.matrix = new char[matrix.length][matrix.length];
        this.keyLine = keyLine;
        this.keyColumn = keyColumn;
        this.exitLine = exitLine;
        this.exitColumn = exitColumn;
        this.traps = traps;
        this.doors = doors;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                this.matrix[i][j] = matrix[i][j];
                if (this.matrix[i][j] == 'H') {
                    heroLine = i;
                    heroColumn = j;
                }
                if (this.matrix[i][j] == 'M') {
                    whiteMummys.add(new Enemy(i, j,'A'));
                }
                if (this.matrix[i][j] == 'V') {
                    redMummys.add(new Enemy(i, j, 'A'));
                }
                if (this.matrix[i][j] == 'E') {
                    scorpions.add(new Enemy(i, j, 'A'));
                }
            }
        }
    }

    @Override
    public void executeAction(Action action) {
        action.execute(this);
        firePuzzleChanged(null);
    }

    public boolean canMoveUp() {
        if(heroLine==1 && matrix[heroLine - 1][heroColumn] == 'S') {    //ve se a linha a cima tem a porta
            return true;
        }
        if(heroLine>1){
            if((matrix[heroLine -1][heroColumn] == ' ' || matrix[heroLine - 1][heroColumn] == '_') && (matrix[heroLine - 2][heroColumn] == '.' || matrix[heroLine - 2][heroColumn] == 'C')){
                return true;
            }
        }
        return false;
    }

    public boolean canMoveRight() {
        if(heroColumn == 11 && matrix[heroLine][heroColumn+1] == 'S') {    //ve se a linha ao lado tem a porta
            return true;
        }
        if(heroColumn<11){
            if((matrix[heroLine][heroColumn+1] == ' ' || matrix[heroLine][heroColumn+1] == ')') && (matrix[heroLine][heroColumn+2] == '.' || matrix[heroLine][heroColumn+2] == 'C')){
                return true;
            }
        }
        return false;
    }

    public boolean canMoveDown() {
        if(heroLine==11 && matrix[heroLine + 1][heroColumn] == 'S') {
            return true;
        }
        if(heroLine<11){
            if((matrix[heroLine +1][heroColumn] == ' ' || matrix[heroLine + 1][heroColumn] == '_') && (matrix[heroLine + 2][heroColumn] == '.' || matrix[heroLine + 2][heroColumn] == 'C')){
                return true;
            }
        }
        return false;
    }

    public boolean canMoveLeft() {
        System.out.println(heroColumn);
        if(heroColumn == 1 && matrix[heroLine][heroColumn-1] == 'S') {
            return true;
        }
        if(heroColumn>1){
            if((matrix[heroLine][heroColumn-1] == ' ' || matrix[heroLine][heroColumn-1] == ')') && (matrix[heroLine][heroColumn-2] == '.' || matrix[heroLine][heroColumn-2] == 'C')){
                return true;
            }
        }
        return false;
    }


    public void moveUp() {
        if(heroLine == 1){
            matrix[heroLine - 1][heroColumn] = 'H';
            matrix[heroLine][heroColumn]='.';
            heroLine -= 1;
            return;
        }
        if(heroLine > 1 ){
            matrix[heroLine - 2][heroColumn] = 'H';
            matrix[heroLine][heroColumn] = '.';
            heroLine -=2;
        }
        if (heroLine == keyLine && heroColumn == keyColumn) {
            portas();
        }
        if(whiteMummys.size() != 0){
            for ( int j= 0; j < whiteMummys.size(); j++ ) {
                if(whiteMummys.get(j).getEstado() != 'M'){
                    moveWhiteMummy(j);
                    if(isMorreu()){
                        return;
                    }
                }
            }
        }
        if(redMummys.size() != 0) {
            for ( int j= 0; j < redMummys.size(); j++ ) {
                if(redMummys.get(j).getEstado() != 'M') {
                    moveRedMummy(j);
                    if(isMorreu()){
                        return;
                    }
                }
            }
        }
        if(scorpions.size() != 0) {
            for ( int j= 0; j < scorpions.size(); j++ ) {
                if(scorpions.get(j).getEstado() != 'M') {
                    moveScorpion(j);
                    if(isMorreu()){
                        return;
                    }
                }
            }
        }
        if(!traps.isEmpty()) {
            for ( int j= 0; j < traps.size(); j++ ) {
                trapLine = traps.get(j).getLine();
                trapColumn = traps.get(j).getColumn();
                if(matrix[trapLine][trapColumn] == '.') {
                    matrix[trapLine][trapColumn] = 'A';
                }
            }
        }
        if(keyLine!=0 && matrix[keyLine][keyColumn] == '.') {
            matrix[keyLine][keyColumn] = 'C';
        }
    }

    public void moveRight() {
        if(heroColumn == 11){
            matrix[heroLine][heroColumn+1] = 'H';
            matrix[heroLine][heroColumn]='.';
            heroColumn+=1;
            return;
        }
        if(heroColumn < 11 ){
            matrix[heroLine][heroColumn+2] = 'H';
            matrix[heroLine][heroColumn] = '.';
            heroColumn+=2;
        }
        if (heroLine == keyLine && heroColumn == keyColumn) {
            portas();
        }
        if(whiteMummys.size() != 0){
            for ( int j= 0; j < whiteMummys.size(); j++ ) {
                if(whiteMummys.get(j).getEstado() != 'M'){
                    moveWhiteMummy(j);
                    if(isMorreu()){
                        return;
                    }
                }
            }
        }
        if(redMummys.size() != 0) {
            for ( int j= 0; j < redMummys.size(); j++ ) {
                if(redMummys.get(j).getEstado() != 'M') {
                    moveRedMummy(j);
                    if(isMorreu()){
                        return;
                    }
                }
            }
        }
        if(scorpions.size() != 0) {
            for ( int j= 0; j < scorpions.size(); j++ ) {
                if(scorpions.get(j).getEstado() != 'M') {
                    moveScorpion(j);
                    if(isMorreu()){
                        return;
                    }
                }
            }
        }
        if(!traps.isEmpty()) {
            for ( int j= 0; j < traps.size(); j++ ) {
                trapLine = traps.get(j).getLine();
                trapColumn = traps.get(j).getColumn();
                if(matrix[trapLine][trapColumn] == '.') {
                    matrix[trapLine][trapColumn] = 'A';
                }
            }
        }
        if(keyLine!=0 && matrix[keyLine][keyColumn] == '.') {
            matrix[keyLine][keyColumn] = 'C';
        }
    }

    public void moveDown() {
        if(heroLine == 11){
            matrix[heroLine + 1][heroColumn] = 'H';
            matrix[heroLine][heroColumn]= '.';
            heroLine += 1;
            return;
        }
        if(heroLine < 11 ){
            matrix[heroLine + 2][heroColumn] = 'H';
            matrix[heroLine][heroColumn] = '.';
            heroLine += 2;
        }
        if (heroLine == keyLine && heroColumn == keyColumn) {
            portas();
        }
        if(whiteMummys.size() != 0){
            for ( int j= 0; j < whiteMummys.size(); j++ ) {
                if(whiteMummys.get(j).getEstado() != 'M'){
                    moveWhiteMummy(j);
                    if(isMorreu()){
                        return;
                    }
                }
            }
        }
        if(redMummys.size() != 0) {
            for ( int j= 0; j < redMummys.size(); j++ ) {
                if(redMummys.get(j).getEstado() != 'M') {
                    moveRedMummy(j);
                    if(isMorreu()){
                        return;
                    }
                }
            }
        }
        if(scorpions.size() != 0) {
            for ( int j= 0; j < scorpions.size(); j++ ) {
                if(scorpions.get(j).getEstado() != 'M') {
                    moveScorpion(j);
                    if(isMorreu()){
                        return;
                    }
                }
            }
        }
        if(!traps.isEmpty()) {
            for ( int j= 0; j < traps.size(); j++ ) {
                trapLine = traps.get(j).getLine();
                trapColumn = traps.get(j).getColumn();
                if(matrix[trapLine][trapColumn] == '.') {
                    matrix[trapLine][trapColumn] = 'A';
                }
            }
        }
        if(keyLine!=0 && matrix[keyLine][keyColumn] == '.') {
            matrix[keyLine][keyColumn] = 'C';
        }
    }

    public void moveLeft() {
        if(heroColumn == 1){
            matrix[heroLine][heroColumn-1] = 'H';
            matrix[heroLine][heroColumn] = '.';
            heroColumn -= 1;
            return;
        }
        if(heroColumn > 1 ){
            matrix[heroLine][heroColumn - 2] = 'H';
            matrix[heroLine][heroColumn] = '.';
            heroColumn -= 2;
        }
        if (heroLine == keyLine && heroColumn == keyColumn) {
            portas();
        }        if(whiteMummys.size() != 0){
            for ( int j= 0; j < whiteMummys.size(); j++ ) {
                if(whiteMummys.get(j).getEstado() != 'M'){
                    moveWhiteMummy(j);
                    if(isMorreu()){
                        return;
                    }
                }
            }
        }
        if(redMummys.size() != 0) {
            for ( int j= 0; j < redMummys.size(); j++ ) {
                if(redMummys.get(j).getEstado() != 'M') {
                    moveRedMummy(j);
                    if(isMorreu()){
                        return;
                    }
                }
            }
        }
        if(scorpions.size() != 0) {
            for ( int j= 0; j < scorpions.size(); j++ ) {
                if(scorpions.get(j).getEstado() != 'M') {
                    moveScorpion(j);
                    if(isMorreu()){
                        return;
                    }
                }
            }
        }
        if(!traps.isEmpty()) {
            for ( int j= 0; j < traps.size(); j++ ) {
                trapLine = traps.get(j).getLine();
                trapColumn = traps.get(j).getColumn();
                if(matrix[trapLine][trapColumn] == '.') {
                    matrix[trapLine][trapColumn] = 'A';
                }
            }
        }
        if(keyLine != 0 && matrix[keyLine][keyColumn] == '.') {
            matrix[keyLine][keyColumn] = 'C';
        }
    }

    public void dontMove(){
        if(whiteMummys.size() != 0){
            for ( int j= 0; j < whiteMummys.size(); j++ ) {
                if(whiteMummys.get(j).getEstado() != 'M'){
                    moveWhiteMummy(j);
                    if(isMorreu()){
                        return;
                    }
                }
            }
        }
        if(redMummys.size() != 0) {
            for ( int j= 0; j < redMummys.size(); j++ ) {
                if(redMummys.get(j).getEstado() != 'M') {
                    moveRedMummy(j);
                    if(isMorreu()){
                        return;
                    }
                }
            }
        }
        if(scorpions.size() != 0) {
            for ( int j= 0; j < scorpions.size(); j++ ) {
                if(scorpions.get(j).getEstado() != 'M') {
                    moveScorpion(j);
                    if(isMorreu()){
                        return;
                    }
                }
            }
        }
        if(!traps.isEmpty()) {
            for ( int j= 0; j < traps.size(); j++ ) {
                trapLine = traps.get(j).getLine();
                trapColumn = traps.get(j).getColumn();
                if(matrix[trapLine][trapColumn] == '.') {
                    matrix[trapLine][trapColumn] = 'A';
                }
            }
        }
        if(keyLine!=0 && matrix[keyLine][keyColumn] == '.') {
            matrix[keyLine][keyColumn] = 'C';
        }
    }


    public void moveWhiteMummy(int j){

        whiteMummyLine = whiteMummys.get(j).getLine();
        whiteMummyColumn = whiteMummys.get(j).getColumn();

        for (int i = 0; i < 2; i++) {

            if (whiteMummyColumn == heroColumn) {

                if (whiteMummyLine < heroLine && (matrix[whiteMummyLine + 1][whiteMummyColumn] == ' ' || matrix[whiteMummyLine + 1][whiteMummyColumn] == '_')) {

                    if (matrix[whiteMummyLine + 2][whiteMummyColumn] == 'M' || matrix[whiteMummyLine + 2][whiteMummyColumn] == 'V') {
                        whiteMummys.get(j).setEstado('M');
                        matrix[whiteMummyLine][whiteMummyColumn] = '.';
                        return;
                    }
                    if (matrix[whiteMummyLine + 2][whiteMummyColumn] == 'E'){
                        for ( int x= 0; x < scorpions.size(); x++ ) {
                            if(scorpions.get(x).getLine() == whiteMummyLine + 2 && scorpions.get(x).getColumn() == whiteMummyColumn && scorpions.get(x).getEstado() == 'A'){
                                scorpions.get(x).setEstado('M');
                            }
                        }
                    }

                    //mumia está acima do heroi
                    matrix[whiteMummyLine + 2][whiteMummyColumn] = 'M';
                    matrix[whiteMummyLine][whiteMummyColumn] = '.';
                    whiteMummyLine += 2;
                    if (whiteMummyLine == keyLine && whiteMummyColumn == keyColumn) {
                        portas();
                    }

                } else if (whiteMummyLine > heroLine && (matrix[whiteMummyLine - 1][whiteMummyColumn] == ' ' || matrix[whiteMummyLine - 1][whiteMummyColumn] == '_')) {

                    if (matrix[whiteMummyLine - 2][whiteMummyColumn] == 'M' || matrix[whiteMummyLine - 2][whiteMummyColumn] == 'V') {
                        whiteMummys.get(j).setEstado('M');
                        matrix[whiteMummyLine][whiteMummyColumn] = '.';
                        return;
                    }
                    if (matrix[whiteMummyLine - 2][whiteMummyColumn] == 'E'){
                        for ( int x= 0; x < scorpions.size(); x++ ) {
                            if(scorpions.get(x).getLine() == whiteMummyLine - 2 && scorpions.get(x).getColumn() == whiteMummyColumn && scorpions.get(x).getEstado() == 'A'){
                                scorpions.get(x).setEstado('M');
                            }
                        }
                    }

                    //mumia está abaixo do heroi
                    matrix[whiteMummyLine - 2][whiteMummyColumn] = 'M';
                    matrix[whiteMummyLine][whiteMummyColumn] = '.';
                    whiteMummyLine -= 2;
                    if (whiteMummyLine == keyLine && whiteMummyColumn == keyColumn) {
                        portas();
                    }
                }

            } else if (whiteMummyColumn > heroColumn && (matrix[whiteMummyLine][whiteMummyColumn - 1] == ' ' || matrix[whiteMummyLine][whiteMummyColumn - 1] == ')')) {

                if (matrix[whiteMummyLine][whiteMummyColumn - 2] == 'M' || matrix[whiteMummyLine][whiteMummyColumn - 2] == 'V') {
                    whiteMummys.get(j).setEstado('M');
                    matrix[whiteMummyLine][whiteMummyColumn] = '.';
                    return;
                }
                if (matrix[whiteMummyLine][whiteMummyColumn - 2] == 'E'){
                    for ( int x= 0; x < scorpions.size(); x++ ) {
                        if(scorpions.get(x).getLine() == whiteMummyLine && scorpions.get(x).getColumn() == whiteMummyColumn - 2 && scorpions.get(x).getEstado() == 'A'){
                            scorpions.get(x).setEstado('M');
                        }
                    }
                }

                //mumia está à direita do heroi
                matrix[whiteMummyLine][whiteMummyColumn - 2] = 'M';
                matrix[whiteMummyLine][whiteMummyColumn] = '.';
                whiteMummyColumn -= 2;
                if (whiteMummyLine == keyLine && whiteMummyColumn == keyColumn) {
                    portas();
                }

            } else if (whiteMummyColumn < heroColumn && (matrix[whiteMummyLine][whiteMummyColumn + 1] == ' ' || matrix[whiteMummyLine][whiteMummyColumn + 1] == ')')) {

                //está à esquerda
                if (matrix[whiteMummyLine][whiteMummyColumn + 2] == 'M' || matrix[whiteMummyLine][whiteMummyColumn + 2] == 'V') {
                    whiteMummys.get(j).setEstado('M');
                    matrix[whiteMummyLine][whiteMummyColumn] = '.';
                    return;
                }
                if (matrix[whiteMummyLine][whiteMummyColumn + 2] == 'E'){
                    for ( int x= 0; x < scorpions.size(); x++ ) {
                        if(scorpions.get(x).getLine() == whiteMummyLine && scorpions.get(x).getColumn() == whiteMummyColumn + 2 && scorpions.get(x).getEstado() == 'A'){
                            scorpions.get(x).setEstado('M');
                        }
                    }
                }

                matrix[whiteMummyLine][whiteMummyColumn + 2] = 'M';
                matrix[whiteMummyLine][whiteMummyColumn] = '.';
                whiteMummyColumn += 2;
                if (whiteMummyLine == keyLine && whiteMummyColumn == keyColumn) {
                    portas();
                }

            } else {
                if (whiteMummyLine < heroLine && (matrix[whiteMummyLine + 1][whiteMummyColumn] == ' ' || matrix[whiteMummyLine + 1][whiteMummyColumn] == '_')) {

                    if (matrix[whiteMummyLine + 2][whiteMummyColumn] == 'M' || matrix[whiteMummyLine + 2][whiteMummyColumn] == 'V') {
                        whiteMummys.get(j).setEstado('M');
                        matrix[whiteMummyLine][whiteMummyColumn] = '.';
                        return;
                    }
                    if (matrix[whiteMummyLine + 2][whiteMummyColumn] == 'E'){
                        for ( int x= 0; x < scorpions.size(); x++ ) {
                            if(scorpions.get(x).getLine() == whiteMummyLine + 2 && scorpions.get(x).getColumn() == whiteMummyColumn && scorpions.get(x).getEstado() == 'A'){
                                scorpions.get(x).setEstado('M');
                            }
                        }
                    }

                    //mumia está acima do heroi
                    matrix[whiteMummyLine + 2][whiteMummyColumn] = 'M';
                    matrix[whiteMummyLine][whiteMummyColumn] = '.';
                    whiteMummyLine += 2;
                    if (whiteMummyLine == keyLine && whiteMummyColumn == keyColumn) {
                        portas();
                    }

                } else if (whiteMummyLine > heroLine && (matrix[whiteMummyLine - 1][whiteMummyColumn] == ' ' || matrix[whiteMummyLine - 1][whiteMummyColumn] == '_')) {

                    if (matrix[whiteMummyLine - 2][whiteMummyColumn] == 'M' || matrix[whiteMummyLine - 2][whiteMummyColumn] == 'V') {
                        whiteMummys.get(j).setEstado('M');
                        matrix[whiteMummyLine][whiteMummyColumn] = '.';
                        return;
                    }
                    if (matrix[whiteMummyLine - 2][whiteMummyColumn] == 'E'){
                        for ( int x= 0; x < scorpions.size(); x++ ) {
                            if(scorpions.get(x).getLine() == whiteMummyLine - 2 && scorpions.get(x).getColumn() == whiteMummyColumn && scorpions.get(x).getEstado() == 'A'){
                                scorpions.get(x).setEstado('M');
                            }
                        }
                    }

                    //mumia está abaixo do heroi
                    matrix[whiteMummyLine - 2][whiteMummyColumn] = 'M';
                    matrix[whiteMummyLine][whiteMummyColumn] = '.';
                    whiteMummyLine -= 2;
                    if (whiteMummyLine == keyLine && whiteMummyColumn == keyColumn) {
                        portas();
                    }

                }
            }
            whiteMummys.get(j).setLine(whiteMummyLine);
            whiteMummys.get(j).setColumn(whiteMummyColumn);



            if (whiteMummyLine == heroLine && whiteMummyColumn == heroColumn) {

                morreu = true;
                return;
            }
        }
    }

    public void moveRedMummy(int j){

        redMummyLine = redMummys.get(j).getLine();
        redMummyColumn = redMummys.get(j).getColumn();

        for (int i = 0; i < 2; i++) {

            if (redMummyLine == heroLine) {

                if (redMummyColumn > heroColumn && (matrix[redMummyLine][redMummyColumn - 1] == ' ' || matrix[redMummyLine][redMummyColumn - 1] == ')')) {

                    if (matrix[redMummyLine][redMummyColumn - 2] == 'V' || matrix[redMummyLine][redMummyColumn - 2] == 'M') {
                        redMummys.get(j).setEstado('M');
                        matrix[redMummyLine][redMummyColumn] = '.';
                        return;
                    }
                    if (matrix[redMummyLine][redMummyColumn - 2] == 'E'){
                        for ( int x= 0; x < scorpions.size(); x++ ) {
                            if(scorpions.get(x).getLine() == redMummyLine && scorpions.get(x).getColumn() == redMummyColumn - 2 && scorpions.get(x).getEstado() == 'A'){
                                scorpions.get(x).setEstado('M');
                            }
                        }
                    }

                    matrix[redMummyLine][redMummyColumn - 2] = 'V';
                    matrix[redMummyLine][redMummyColumn] = '.';
                    redMummyColumn -= 2;
                    if (redMummyLine == keyLine && redMummyColumn == keyColumn) {
                        portas();
                    }


                } else if (redMummyColumn < heroColumn && (matrix[redMummyLine][redMummyColumn + 1] == ' ' || matrix[redMummyLine][redMummyColumn + 1] == ')')) {

                    if (matrix[redMummyLine][redMummyColumn + 2] == 'V' || matrix[redMummyLine][redMummyColumn + 2] == 'M') {
                        redMummys.get(j).setEstado('M');
                        matrix[redMummyLine][redMummyColumn] = '.';
                        return;
                    }
                    if (matrix[redMummyLine][redMummyColumn + 2] == 'E'){
                        for ( int x= 0; x < scorpions.size(); x++ ) {
                            if(scorpions.get(x).getLine() == redMummyLine && scorpions.get(x).getColumn() == redMummyColumn + 2 && scorpions.get(x).getEstado() == 'A'){
                                scorpions.get(x).setEstado('M');
                            }
                        }
                    }

                    //está à esquerda
                    matrix[redMummyLine][redMummyColumn + 2] = 'V';
                    matrix[redMummyLine][redMummyColumn] = '.';
                    redMummyColumn += 2;
                    if (redMummyLine == keyLine && redMummyColumn == keyColumn) {
                        portas();
                    }

                }

            } else if (redMummyLine < heroLine && (matrix[redMummyLine + 1][redMummyColumn] == ' ' || matrix[redMummyLine + 1][redMummyColumn] == '_')) {

                if (matrix[redMummyLine + 2][redMummyColumn] == 'V' || matrix[redMummyLine + 2][redMummyColumn] == 'M') {
                    redMummys.get(j).setEstado('M');
                    matrix[redMummyLine][redMummyColumn] = '.';
                    return;
                }
                if (matrix[redMummyLine + 2][redMummyColumn] == 'E'){
                    for ( int x= 0; x < scorpions.size(); x++ ) {
                        if(scorpions.get(x).getLine() == redMummyLine + 2 && scorpions.get(x).getColumn() == redMummyColumn && scorpions.get(x).getEstado() == 'A'){
                            scorpions.get(x).setEstado('M');
                        }
                    }
                }

                //mumia está acima do heroi
                matrix[redMummyLine + 2][redMummyColumn] = 'V';
                matrix[redMummyLine][redMummyColumn] = '.';
                redMummyLine += 2;
                if (redMummyLine == keyLine && redMummyColumn == keyColumn) {
                    portas();
                }

            } else if (redMummyLine > heroLine && (matrix[redMummyLine - 1][redMummyColumn] == ' ' || matrix[redMummyLine - 1][redMummyColumn] == '_')) {

                if (matrix[redMummyLine - 2][redMummyColumn] == 'V' || matrix[redMummyLine - 2][redMummyColumn] == 'M') {
                    redMummys.get(j).setEstado('M');
                    matrix[redMummyLine][redMummyColumn] = '.';
                    return;
                }
                if (matrix[redMummyLine - 2][redMummyColumn] == 'E'){
                    for ( int x= 0; x < scorpions.size(); x++ ) {
                        if(scorpions.get(x).getLine() == redMummyLine - 2 && scorpions.get(x).getColumn() == redMummyColumn && scorpions.get(x).getEstado() == 'A'){
                            scorpions.get(x).setEstado('M');
                        }
                    }
                }

                //mumia está abaixo do heroi
                matrix[redMummyLine - 2][redMummyColumn] = 'V';
                matrix[redMummyLine][redMummyColumn] = '.';
                redMummyLine -= 2;
                if (redMummyLine == keyLine && redMummyColumn == keyColumn) {
                    portas();
                }

            } else {

                if (redMummyColumn > heroColumn && (matrix[redMummyLine][redMummyColumn - 1] == ' ' || matrix[redMummyLine][redMummyColumn - 1] == ')')) {

                    if (matrix[redMummyLine][redMummyColumn - 2] == 'V' || matrix[redMummyLine][redMummyColumn - 2] == 'M') {
                        redMummys.get(j).setEstado('M');
                        matrix[redMummyLine][redMummyColumn] = '.';
                        return;
                    }
                    if (matrix[redMummyLine][redMummyColumn - 2] == 'E'){
                        for ( int x= 0; x < scorpions.size(); x++ ) {
                            if(scorpions.get(x).getLine() == redMummyLine && scorpions.get(x).getColumn() == redMummyColumn - 2 && scorpions.get(x).getEstado() == 'A'){
                                scorpions.get(x).setEstado('M');
                            }
                        }
                    }

                    matrix[redMummyLine][redMummyColumn - 2] = 'V';
                    matrix[redMummyLine][redMummyColumn] = '.';
                    redMummyColumn -= 2;
                    if (redMummyLine == keyLine && redMummyColumn == keyColumn) {
                        portas();
                    }

                } else if (redMummyColumn < heroColumn && (matrix[redMummyLine][redMummyColumn + 1] == ' ' || matrix[redMummyLine][redMummyColumn + 1] == ')')) {

                    if (matrix[redMummyLine][redMummyColumn + 2] == 'V' || matrix[redMummyLine][redMummyColumn + 2] == 'M') {
                        redMummys.get(j).setEstado('M');
                        matrix[redMummyLine][redMummyColumn] = '.';
                        return;
                    }
                    if (matrix[redMummyLine][redMummyColumn + 2] == 'E'){
                        for ( int x= 0; x < scorpions.size(); x++ ) {
                            if(scorpions.get(x).getLine() == redMummyLine && scorpions.get(x).getColumn() == redMummyColumn + 2 && scorpions.get(x).getEstado() == 'A'){
                                scorpions.get(x).setEstado('M');
                            }
                        }
                    }

                    //está à esquerda
                    matrix[redMummyLine][redMummyColumn + 2] = 'V';
                    matrix[redMummyLine][redMummyColumn] = '.';
                    redMummyColumn += 2;
                    if (redMummyLine == keyLine && redMummyColumn == keyColumn) {
                        portas();
                    }

                }
            }

            redMummys.get(j).setLine(redMummyLine);
            redMummys.get(j).setColumn(redMummyColumn);


            if (redMummyLine == heroLine && redMummyColumn == heroColumn) {

                morreu = true;
                return;
            }
        }
    }

    public void moveScorpion(int j){

        scorpionLine = scorpions.get(j).getLine();
        scorpionColumn = scorpions.get(j).getColumn();

        if (scorpionColumn == heroColumn) {

            if (scorpionLine < heroLine && (matrix[scorpionLine + 1][scorpionColumn] == ' ' || matrix[scorpionLine + 1][scorpionColumn] == '_')) {

                if (matrix[scorpionLine + 2][scorpionColumn] == 'E' || matrix[scorpionLine + 2][scorpionColumn] == 'M' || matrix[scorpionLine + 2][scorpionColumn] == 'V') {
                    scorpions.get(j).setEstado('M');
                    matrix[scorpionLine][scorpionColumn] = '.';
                    return;
                }

                //escorpiao está acima do heroi
                matrix[scorpionLine + 2][scorpionColumn] = 'E';
                matrix[scorpionLine][scorpionColumn] = '.';
                scorpionLine += 2;
                if (scorpionLine == keyLine && scorpionColumn == keyColumn) {
                    portas();
                }

            } else if (scorpionLine > heroLine && (matrix[scorpionLine - 1][scorpionColumn] == ' ' || matrix[scorpionLine - 1][scorpionColumn] == '_')) {

                if (matrix[scorpionLine - 2][scorpionColumn ] == 'E' || matrix[scorpionLine - 2][scorpionColumn] == 'M' || matrix[scorpionLine - 2][scorpionColumn] == 'V') {
                    scorpions.get(j).setEstado('M');
                    matrix[scorpionLine][scorpionColumn] = '.';
                    return;
                }

                //mumia está abaixo do heroi
                matrix[scorpionLine - 2][scorpionColumn] = 'E';
                matrix[scorpionLine][scorpionColumn] = '.';
                scorpionLine -= 2;
                if (scorpionLine == keyLine && scorpionColumn == keyColumn) {
                    portas();
                }

            }

        } else if (scorpionColumn > heroColumn && (matrix[scorpionLine][scorpionColumn - 1] == ' ' || matrix[scorpionLine][scorpionColumn - 1] == ')')) {

            if (matrix[scorpionLine][scorpionColumn - 2] == 'E' || matrix[scorpionLine][scorpionColumn - 2] == 'M' || matrix[scorpionLine][scorpionColumn - 2] == 'V') {
                scorpions.get(j).setEstado('M');
                matrix[scorpionLine][scorpionColumn] = '.';
                return;
            }

            //mumia está à direita do heroi
            matrix[scorpionLine][scorpionColumn - 2] = 'E';
            matrix[scorpionLine][scorpionColumn] = '.';
            scorpionColumn -= 2;
            if (scorpionLine == keyLine && scorpionColumn == keyColumn) {
                portas();
            }

        } else if (scorpionColumn < heroColumn && (matrix[scorpionLine][scorpionColumn + 1] == ' ' || matrix[scorpionLine][scorpionColumn + 1] == ')')) {

            if (matrix[scorpionLine][scorpionColumn + 2] == 'E' || matrix[scorpionLine][scorpionColumn + 2] == 'M' || matrix[scorpionLine][scorpionColumn + 2] == 'V') {
                scorpions.get(j).setEstado('M');
                matrix[scorpionLine][scorpionColumn] = '.';
                return;
            }

            //está à esquerda
            matrix[scorpionLine][scorpionColumn + 2] = 'E';
            matrix[scorpionLine][scorpionColumn] = '.';
            scorpionColumn += 2;
            if (scorpionLine == keyLine && scorpionColumn == keyColumn) {
                portas();
            }

        } else {
            if (scorpionLine < heroLine && (matrix[scorpionLine + 1][scorpionColumn] == ' ' || matrix[scorpionLine + 1][scorpionColumn] == '_')) {

                if (matrix[scorpionLine + 2][scorpionColumn] == 'E' || matrix[scorpionLine + 2][scorpionColumn] == 'M' || matrix[scorpionLine + 2][scorpionColumn] == 'V') {
                    scorpions.get(j).setEstado('M');
                    matrix[scorpionLine][scorpionColumn] = '.';
                    return;
                }

                //mumia está acima do heroi
                matrix[scorpionLine + 2][scorpionColumn] = 'E';
                matrix[scorpionLine][scorpionColumn] = '.';
                scorpionLine += 2;
                if (scorpionLine == keyLine && scorpionColumn == keyColumn) {
                    portas();
                }


            } else if (scorpionLine > heroLine && (matrix[scorpionLine - 1][scorpionColumn] == ' ' || matrix[scorpionLine - 1][scorpionColumn] == '_')) {

                if (matrix[scorpionLine - 2][scorpionColumn] == 'E' || matrix[scorpionLine - 2][scorpionColumn] == 'M' || matrix[scorpionLine - 2][scorpionColumn] == 'V') {
                    scorpions.get(j).setEstado('M');
                    matrix[scorpionLine][scorpionColumn] = '.';
                    return;
                }

                //mumia está abaixo do heroi
                matrix[scorpionLine - 2][scorpionColumn] = 'E';
                matrix[scorpionLine][scorpionColumn] = '.';
                scorpionLine -= 2;
                if (scorpionLine == keyLine && scorpionColumn == keyColumn) {
                    portas();
                }

            }
        }

        scorpions.get(j).setLine(scorpionLine);
        scorpions.get(j).setColumn(scorpionColumn);



        if (scorpionLine == heroLine && scorpionColumn == heroColumn) {

            morreu = true;
            return;
        }
    }


    public void portas(){
        for ( int j= 0; j < doors.size(); j++ ) {
            doorLine = doors.get(j).getLine();
            doorColumn = doors.get(j).getColumn();
            switch (matrix[doorLine][doorColumn]) {
                case '=' :
                    matrix[doorLine][doorColumn] = '_';
                    break;
                case '_' :
                    matrix[doorLine][doorColumn] = '=';
                    break;
                case '"' :
                    matrix[doorLine][doorColumn] = ')';
                    break;
                case ')' :
                    matrix[doorLine][doorColumn] = '"';
                    break;
            }
            
        }
    }


    public double computeDistanciaSaida() {

        if (morreu){
            return Double.MAX_VALUE;
        }
        int dis = Math.abs(heroLine - exitLine) + Math.abs(heroColumn - exitColumn);

        return dis;
    }

    public double computeDistanciaInimigoProximo() {

        final double max = ((matrix.length - 2) - 1) * 2;
        double h = max;
        double aux = max + 1;
        int enemyLine;
        int enemyColumn;

        if (morreu){
            return Double.MAX_VALUE;
        }
        if (heroLine == exitLine && heroColumn == exitColumn){
            return 0;
        }

        for ( int i= 0; i < whiteMummys.size(); i++ ) {
            enemyLine = whiteMummys.get(i).getLine();
            enemyColumn = whiteMummys.get(i).getColumn();
            if(whiteMummys.get(i).getEstado() == 'M') {
                continue;
            }
            aux = Math.abs(heroLine - enemyLine) + Math.abs(heroColumn - enemyColumn);
            if (aux < h) {
                h = aux;
            }
        }
        for ( int i= 0; i < redMummys.size(); i++ ) {
            enemyLine = redMummys.get(i).getLine();
            enemyColumn = redMummys.get(i).getColumn();
            if(redMummys.get(i).getEstado() == 'M') {
                continue;
            }
            aux = Math.abs(heroLine - enemyLine) + Math.abs(heroColumn - enemyColumn);
            if (aux < h) {
                h = aux;
            }
        }
        for ( int i= 0; i < scorpions.size(); i++ ) {
            enemyLine = scorpions.get(i).getLine();
            enemyColumn = scorpions.get(i).getColumn();
            if (scorpions.get(i).getEstado() == 'M') {
                continue;
            }
            aux = Math.abs(heroLine - enemyLine) + Math.abs(heroColumn - enemyColumn);
            if (aux < h) {
                h = aux;
            }
        }
        System.out.println("distancia inimigo mais proximo: " + h);
        return (aux > max ? 0 : max - h);

    }

    public int getNumLines() {
        return matrix.length;
    }

    public int getNumColumns() {
        return matrix[0].length;
    }

    public int getTileValue(int line, int column) {
        if (!isValidPosition(line, column)) {
            throw new IndexOutOfBoundsException("Invalid position!");
        }
        return matrix[line][column];
    }

    public boolean isValidPosition(int line, int column) {
        return line >= 0 && line < matrix.length && column >= 0 && column < matrix[0].length;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof MummyMazeState)) {
            return false;
        }

        MummyMazeState o = (MummyMazeState) other;
        if (matrix.length != o.matrix.length) {
            return false;
        }

        return Arrays.deepEquals(matrix, o.matrix);
    }

    @Override
    public int hashCode() {
        return 97 * 7 + Arrays.deepHashCode(this.matrix);
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            buffer.append('\n');
            for (int j = 0; j < matrix.length; j++) {
                buffer.append(matrix[i][j]);
                buffer.append(' ');
            }
        }
        return buffer.toString();
    }

    @Override
    public MummyMazeState clone() {
        return new MummyMazeState(matrix, keyLine, keyColumn, exitLine, exitColumn, traps, doors);
    }
    //Listeners
    private transient ArrayList<MummyMazeListener> listeners = new ArrayList<MummyMazeListener>(3);

    public synchronized void removeListener(MummyMazeListener l) {
        if (listeners != null && listeners.contains(l)) {
            listeners.remove(l);
        }
    }

    public synchronized void addListener(MummyMazeListener l) {
        if (!listeners.contains(l)) {
            listeners.add(l);
        }
    }

    public void firePuzzleChanged(MummyMazeEvent pe) {
        for (MummyMazeListener listener : listeners) {
            listener.puzzleChanged(null);
        }
    }

    public char[][] getMatrix() {
        return matrix;
    }

    public int getHeroLine() {
        return heroLine;
    }

    public void setHeroLine(int heroLine) {
        this.heroLine = heroLine;
    }

    public int getHeroColumn() {
        return heroColumn;
    }

    public void setHeroColumn(int heroColumn) {
        this.heroColumn = heroColumn;
    }

    public int getExitLine() {
        return exitLine;
    }

    public void setExitLine(int exitLine) {
        this.exitLine = exitLine;
    }

    public int getExitC() {
        return exitColumn;
    }

    public void setExitC(int exitC) {
        this.exitColumn = exitC;
    }

    public boolean isMorreu() {
        return morreu;
    }

}
