-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 07, 2024 at 03:51 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `game2048`
--

-- --------------------------------------------------------

--
-- Table structure for table `bgm`
--

CREATE TABLE `bgm` (
  `bgm_id` int(11) NOT NULL,
  `file_path` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bgm`
--

INSERT INTO `bgm` (`bgm_id`, `file_path`) VALUES
(1, 'Assets\\bgm2048.wav');

-- --------------------------------------------------------

--
-- Table structure for table `gif`
--

CREATE TABLE `gif` (
  `gif_id` int(11) NOT NULL,
  `file_path` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `gif`
--

INSERT INTO `gif` (`gif_id`, `file_path`) VALUES
(1, 'Assets\\starfall-gif-21.gif');

-- --------------------------------------------------------

--
-- Table structure for table `highest_score`
--

CREATE TABLE `highest_score` (
  `score_id` int(11) NOT NULL,
  `highest_score` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bgm`
--
ALTER TABLE `bgm`
  ADD PRIMARY KEY (`bgm_id`);

--
-- Indexes for table `gif`
--
ALTER TABLE `gif`
  ADD PRIMARY KEY (`gif_id`);

--
-- Indexes for table `highest_score`
--
ALTER TABLE `highest_score`
  ADD PRIMARY KEY (`score_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bgm`
--
ALTER TABLE `bgm`
  MODIFY `bgm_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `gif`
--
ALTER TABLE `gif`
  MODIFY `gif_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `highest_score`
--
ALTER TABLE `highest_score`
  MODIFY `score_id` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
