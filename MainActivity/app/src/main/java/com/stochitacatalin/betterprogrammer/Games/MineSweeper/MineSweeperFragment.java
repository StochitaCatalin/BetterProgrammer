package com.stochitacatalin.betterprogrammer.Games.MineSweeper;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.stochitacatalin.betterprogrammer.R;

import java.util.Random;

public class MineSweeperFragment extends Fragment {
    GameStatus gameStatus = GameStatus.run;
    Cell[][] cells;
    int cellsCount = 10;
    int minesCount = 10;
    int unveiled;
    MineSweeperAdapter mineSweeperAdapter;
    GridView gridView;

    public interface OnGameEnd{
        void onWon();
        void onLost();
    }

    public OnGameEnd onGameEnd = new OnGameEnd() {
        @Override
        public void onWon() {

        }

        @Override
        public void onLost() {

        }
    };
    public OnGameEnd defaultOnGameEnd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_minesweeper, container, false);
        gridView = rootView.findViewById(R.id.gridView);
        gridView.setNumColumns(cellsCount);
        initCells(cellsCount,minesCount);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(gameStatus == GameStatus.run) {
                    Cell cell = (Cell) parent.getItemAtPosition(position);
                    if (!cell.marked) {
                        cell.visible = true;
                        if (cell.type == Cell.CeilType.mine) {
                            //ENDGAME
                            gameStatus = GameStatus.lost;
                            unveilMines();
                            defaultOnGameEnd.onLost();
                            onGameEnd.onLost();
                        }
                        else if(cell.type == Cell.CeilType.empty){
                            unveiled++;
                            if (cell.mines == 0)
                                neightboursExpand(position%cellsCount, position/cellsCount);
                            if (unveiled == cellsCount * cellsCount - minesCount) {
                                gameStatus = GameStatus.won;
                                defaultOnGameEnd.onWon();
                                onGameEnd.onWon();
                            }
                        }
                    }
                }
                mineSweeperAdapter.notifyDataSetChanged();
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(gameStatus == GameStatus.run) {
                    Cell cell = (Cell) parent.getItemAtPosition(position);
                    if (!cell.visible)
                        cell.marked = !cell.marked;
                }
                mineSweeperAdapter.notifyDataSetChanged();
                return true;
            }
        });

        defaultOnGameEnd = new OnGameEnd() {
            @Override
            public void onLost() {

                new AlertDialog.Builder(getContext())
                        .setTitle("You Lost!")
                        .setMessage("Want to try it again?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startNewGame();
                            }
                        })
                        .show();
            }

            @Override
            public void onWon() {
                new AlertDialog.Builder(getContext())
                        .setTitle("You Won!")
                        .setMessage("Want to try it again?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startNewGame();
                            }
                        })
                        .setNegativeButton(android.R.string.no,null)
                        .show();
            }
        };

        return rootView;
    }



    public void unveilMines(){
        for(Cell[] cells2 : cells){
            for(Cell cell : cells2){
                if(cell.marked)
                    cell.marked = false;
                if(!cell.visible)
                    cell.visible = true;
            }
        }
    }

    void initCells(int cellsCount,int minesCount) {
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

        mineSweeperAdapter = new MineSweeperAdapter(getContext(),cells,cellsCount);
        gridView.setAdapter(mineSweeperAdapter);
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


    void neightboursExpand(int x, int y) {
        for (int i = y - 1; i <= y + 1; i++)
            for (int j = x - 1; j <= x + 1; j++) {

                try {
                    if(cells[i][j].type == Cell.CeilType.empty && !cells[i][j].visible && !cells[i][j].marked) {
                        cells[i][j].visible = true;
                        unveiled++;
                        if (cells[i][j].mines == 0) {
                            neightboursExpand(j, i);
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }
    }

    public void startNewGame(){
        initCells(cellsCount,minesCount);
        mineSweeperAdapter.notifyDataSetChanged();
        gameStatus = GameStatus.run;
        this.unveiled = 0;
    }


    enum GameStatus{run,lost,won}
}
