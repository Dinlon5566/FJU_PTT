-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- 主機： localhost
-- 產生時間： 2023-01-01 21:12:17
-- 伺服器版本： 8.0.31
-- PHP 版本： 7.4.32

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫： `fju_ptt`
--

-- --------------------------------------------------------

--
-- 資料表結構 `articles`
--

CREATE TABLE `articles` (
  `board` varchar(45) NOT NULL,
  `idArticles` varchar(255) NOT NULL,
  `writer` varchar(45) NOT NULL,
  `title` varchar(45) DEFAULT NULL,
  `time` varchar(45) DEFAULT NULL,
  `body` varchar(15535) DEFAULT NULL,
  `IP` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `cache_idip`
--

CREATE TABLE `cache_idip` (
  `userID` varchar(45) COLLATE utf8mb4_general_ci NOT NULL,
  `IP` varchar(45) COLLATE utf8mb4_general_ci NOT NULL,
  `times` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `cache_ip`
--

CREATE TABLE `cache_ip` (
  `target` varchar(45) COLLATE utf8mb4_general_ci NOT NULL,
  `IP` varchar(45) COLLATE utf8mb4_general_ci NOT NULL,
  `userID` varchar(45) COLLATE utf8mb4_general_ci NOT NULL,
  `times` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `messages`
--

CREATE TABLE `messages` (
  `board` varchar(45) NOT NULL,
  `idArticles` varchar(255) NOT NULL,
  `idMessage` int NOT NULL,
  `userID` varchar(45) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `push_tag` varchar(45) DEFAULT NULL,
  `IP` varchar(45) DEFAULT NULL,
  `time` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- 已傾印資料表的索引
--

--
-- 資料表索引 `articles`
--
ALTER TABLE `articles`
  ADD PRIMARY KEY (`board`,`idArticles`);

--
-- 資料表索引 `cache_idip`
--
ALTER TABLE `cache_idip`
  ADD PRIMARY KEY (`userID`,`IP`);

--
-- 資料表索引 `cache_ip`
--
ALTER TABLE `cache_ip`
  ADD PRIMARY KEY (`IP`,`userID`);

--
-- 資料表索引 `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`board`,`idArticles`,`idMessage`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
