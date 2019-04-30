package com.stochitacatalin.mainactivity.Games.MineSweeper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class MineSweeperBoard extends View implements View.OnTouchListener {
    Cell[][] cells;
    int size, cellSize, padding;
    long downTime;
    GameStatus gameStatus;
    GameStatusView gameStatusView;
    int cellsCount,minesCount;
    int unveiled;

    public MineSweeperBoard(Context context) {
        super(context);
    }

    public MineSweeperBoard(Context context, int cellsCount,int minesCount) {
        this(context);
        this.cellsCount = cellsCount;
        this.minesCount = minesCount;
        gameStatusView = new GameStatusView(getContext());
        startNewGame();
    }

    public void startNewGame(){
        initCells();
        gameStatus = GameStatus.run;
        this.unveiled = 0;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        for (Cell[] cells2 : cells) {
            for (Cell cell : cells2) {
                canvas.save();
                canvas.translate(cell.getX(), cell.getY());
                cell.draw(canvas);
                canvas.restore();
            }
        }
        canvas.restore();
        /*} else */
        if (gameStatus == GameStatus.won) {
            canvas.save();
            showGameeEnd(canvas, "YOU WON");
            canvas.restore();
        } else if (gameStatus == GameStatus.lost) {
            canvas.save();
            showGameeEnd(canvas, "YOU LOST");
            canvas.restore();
        }
    }

    public void showGameeEnd(Canvas canvas,String message){
        gameStatusView.setText(message);
        canvas.save();
        canvas.translate(gameStatusView.getX(),gameStatusView.getY());
        gameStatusView.draw(canvas);
        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        size = MeasureSpec.getSize(widthMeasureSpec);
        cellSize = size / cells.length;
        padding = cellSize / 10;

        setCellsSize();

        gameStatusView.measure(MeasureSpec.makeMeasureSpec((int) (size * 0.8f), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec((int) (size * 0.4f), MeasureSpec.EXACTLY));
        gameStatusView.layout((int) (size * 0.1f), (int) (size * 0.3f), (int) (size * 0.9f), (int) (size * 0.7f));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) (event.getX() / cellSize), y = (int) (event.getY() / cellSize);
        if (cells[y][x].type == Cell.CeilType.mine) {
            gameStatus = GameStatus.lost;
            unveilMines();
        }
        else {
            unveiled++;
            if (cells[y][x].mines == 0)
                neightboursExpand(x, y);
            if (unveiled == cellsCount * cellsCount - minesCount)
                gameStatus = GameStatus.won;
        }
        invalidate();
        return true;
    }

    public void unveilMines(){
        for(Cell[] cells2 : cells){
            for(Cell cell : cells2){
                if(cell.marked)
                    cell.onLongClick(null);
                if(!cell.visible)
                    cell.visible = true;
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getActionIndex() != 0)
            return true;
        if(event.getAction() == MotionEvent.ACTION_DOWN)
            downTime = System.currentTimeMillis();
        else if(event.getAction() == MotionEvent.ACTION_UP){
            if(gameStatus == GameStatus.run) {
                try {
                    if (System.currentTimeMillis() - downTime > 500) {
                        boolean r = cells[(int) (event.getY() / cellSize)][(int) (event.getX() / cellSize)].onLongClick(null);
                        invalidate();
                    } else {
                        boolean r = cells[(int) (event.getY() / cellSize)][(int) (event.getX() / cellSize)].onClick(null);
                        if (r)
                            onTouch(this, event);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    return true;
                }
            }
            else if(gameStatus == GameStatus.won || gameStatus == GameStatus.lost){
                Rect temp = new Rect();
                gameStatusView.getHitRect(temp);
                if(temp.contains((int) event.getX(),(int)event.getY()))
                    if(gameStatusView.onClick((int)(event.getX()-gameStatusView.getX()), (int) (event.getY()-gameStatusView.getY()))){
                        //START NEW GAME
                        startNewGame();
                    }
            }
        }
        return true;
    }

    void neightboursExpand(int x, int y) {
        for (int i = y - 1; i <= y + 1; i++)
            for (int j = x - 1; j <= x + 1; j++) {

                try {
                    if(cells[i][j].type == Cell.CeilType.empty && !cells[i][j].visible) {
                            boolean r = cells[i][j].onClick(cells[i][j]);
                            if(r)
                                unveiled++;
                            if (cells[i][j].mines == 0) {
                                neightboursExpand(j, i);
                            }
                        }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }
    }
    void initCells() {
        cells = new Cell[cellsCount][cellsCount];
        Random rnd = new Random();
        for (int k = 0; k < minesCount; k++) {
            boolean found = false;
            do {
                int x = rnd.nextInt(cellsCount);
                int y = rnd.nextInt(cellsCount);
                if (cells[x][y] == null) {
                    found = true;
                    cells[x][y] = new MineCell(getContext());
                }
            } while (!found);
        }
        for (int i = 0; i < cellsCount; i++)
            for (int j = 0; j < cellsCount; j++)
                if (cells[i][j] == null) {
                    cells[i][j] = new EmptyCell(getContext(), sumOfMines(i, j, cells));
                }

        setCellsSize();
    }
    int sumOfMines(int x,int y,Cell[][] cells){
        int mines = 0;
        for(int i = x-1;i<=x+1;i++)
            for(int j=y-1;j<=y+1;j++){
                if(i>=0 && j>= 0 && i<cellsCount&&j<cellsCount){
                    if(cells[i][j] != null && cells[i][j].type == Cell.CeilType.mine)
                        mines++;
                }
            }
        return mines;
    }

    void setCellsSize(){
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                cells[i][j].measure(MeasureSpec.makeMeasureSpec(cellSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(cellSize, MeasureSpec.EXACTLY));
                int x = j * cellSize;
                int y = i * cellSize;
                cells[i][j].layout(x, y, x + cellSize, y + cellSize);
            }
        }
    }

    enum GameStatus{run,lost,won}
}
