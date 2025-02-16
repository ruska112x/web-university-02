import React, { useEffect, useState, useCallback } from 'react';
import './App.css';

const BOARD_SIZE = 4;

export type Board = number[][];

const initEmptyBoard = (): Board =>
  Array.from({ length: BOARD_SIZE }, () => Array(BOARD_SIZE).fill(0));

const getEmptyCells = (board: Board): { r: number; c: number }[] => {
  const empties: { r: number; c: number }[] = [];
  for (let r = 0; r < BOARD_SIZE; r++) {
    for (let c = 0; c < BOARD_SIZE; c++) {
      if (board[r][c] === 0) empties.push({ r, c });
    }
  }
  return empties;
};

const addRandomTile = (board: Board): Board => {
  const empties = getEmptyCells(board);
  if (empties.length === 0) return board;
  const randomCell = empties[Math.floor(Math.random() * empties.length)];
  const newValue = Math.random() < 0.9 ? 2 : 4;
  const newBoard = board.map(row => row.slice());
  newBoard[randomCell.r][randomCell.c] = newValue;
  return newBoard;
};

const rotateBoard = (board: Board): Board => {
  const newBoard = initEmptyBoard();
  for (let r = 0; r < BOARD_SIZE; r++) {
    for (let c = 0; c < BOARD_SIZE; c++) {
      newBoard[c][BOARD_SIZE - 1 - r] = board[r][c];
    }
  }
  return newBoard;
};

const moveLeft = (
  board: Board
): { newBoard: Board; moved: boolean; scoreGained: number } => {
  let moved = false;
  let scoreGained = 0;
  const newBoard: Board = board.map(row => {
    const filtered = row.filter(val => val !== 0);
    const merged: number[] = [];
    let skip = false;
    for (let i = 0; i < filtered.length; i++) {
      if (skip) {
        skip = false;
        continue;
      }
      if (i < filtered.length - 1 && filtered[i] === filtered[i + 1]) {
        const newVal = filtered[i] * 2;
        merged.push(newVal);
        scoreGained += newVal;
        skip = true;
        moved = true;
      } else {
        merged.push(filtered[i]);
      }
    }
    while (merged.length < BOARD_SIZE) {
      merged.push(0);
    }
    if (merged.toString() !== row.toString()) moved = true;
    return merged;
  });
  return { newBoard, moved, scoreGained };
};

const move = (
  board: Board,
  direction: 'left' | 'right' | 'up' | 'down'
): { newBoard: Board; moved: boolean; scoreGained: number } => {
  let newBoard = board.map(row => row.slice());
  let moved = false;
  let totalScoreGained = 0;
  const rotateTimes = {
    left: 0,
    down: 1,
    right: 2,
    up: 3,
  }[direction];

  for (let i = 0; i < rotateTimes; i++) {
    newBoard = rotateBoard(newBoard);
  }

  const { newBoard: movedBoard, moved: hasMoved, scoreGained } = moveLeft(newBoard);
  moved = hasMoved;
  totalScoreGained = scoreGained;
  newBoard = movedBoard;

  for (let i = 0; i < (4 - rotateTimes) % 4; i++) {
    newBoard = rotateBoard(newBoard);
  }

  return { newBoard, moved, scoreGained: totalScoreGained };
};

const canMove = (board: Board): boolean => {
  if (getEmptyCells(board).length > 0) return true;

  for (let r = 0; r < BOARD_SIZE; r++) {
    for (let c = 0; c < BOARD_SIZE - 1; c++) {
      if (board[r][c] === board[r][c + 1]) return true;
    }
  }
  for (let c = 0; c < BOARD_SIZE; c++) {
    for (let r = 0; r < BOARD_SIZE - 1; r++) {
      if (board[r][c] === board[r + 1][c]) return true;
    }
  }
  return false;
};

const App: React.FC = () => {
  const [board, setBoard] = useState<Board>(() =>
    addRandomTile(addRandomTile(initEmptyBoard()))
  );
  const [gameOver, setGameOver] = useState(false);
  const [score, setScore] = useState(0);

  const handleKeyDown = useCallback(
    (e: KeyboardEvent) => {
      if (gameOver) return;
      let direction: 'left' | 'right' | 'up' | 'down' | null = null;
      switch (e.key) {
        case 'ArrowLeft':
          direction = 'left';
          break;
        case 'ArrowRight':
          direction = 'right';
          break;
        case 'ArrowUp':
          direction = 'down';
          break;
        case 'ArrowDown':
          direction = 'up';
          break;
        default:
          break;
      }
      if (direction) {
        e.preventDefault();
        handleMove(direction);
      }
    },
    [board, gameOver]
  );

  const handleMove = (direction: 'left' | 'right' | 'up' | 'down') => {
    const { newBoard, moved, scoreGained } = move(board, direction);
    if (moved) {
      const boardWithNewTile = addRandomTile(newBoard);
      setBoard(boardWithNewTile);
      setScore(prevScore => prevScore + scoreGained);
      if (!canMove(boardWithNewTile)) {
        setGameOver(true);
      }
    }
  };

  useEffect(() => {
    window.addEventListener('keydown', handleKeyDown);
    return () => {
      window.removeEventListener('keydown', handleKeyDown);
    };
  }, [handleKeyDown]);

  const resetGame = () => {
    setBoard(addRandomTile(addRandomTile(initEmptyBoard())));
    setGameOver(false);
    setScore(0);
  };

  return (
    <div className="game-container">
      <div className="score-board">
        <div className="score">Score: {score}</div>
      </div>
      <div className="board">
        {board.map((row, rIdx) => (
          <div key={rIdx} className="row">
            {row.map((cell, cIdx) => (
              <div
                key={cIdx}
                className={`cell ${cell !== 0 ? `tile-${cell}` : ''}`}
              >
                {cell !== 0 ? cell : ''}
              </div>
            ))}
          </div>
        ))}
      </div>
      <div className="controls">
        <button onClick={() => handleMove('up')}>Up</button>
        <button onClick={() => handleMove('left')}>Left</button>
        <button onClick={() => handleMove('right')}>Right</button>
        <button onClick={() => handleMove('down')}>Down</button>
      </div>
      {gameOver && <div className="game-over">Game Over!</div>}
      <button className="restart-button" onClick={resetGame}>Restart</button>
    </div>
  );
};

export default App;
