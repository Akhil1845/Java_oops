CREATE TABLE `students` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `email` varchar(255) UNIQUE NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(20),
  `address` varchar(255),
  `stream` varchar(100),
  `student_class` varchar(100),
  `created_at` timestamp DEFAULT (now()),
  `updated_at` timestamp
);

CREATE TABLE `interests` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(100) UNIQUE NOT NULL
);

CREATE TABLE `student_interests` (
  `student_id` int NOT NULL,
  `interest_id` int NOT NULL,
  PRIMARY KEY (`student_id`, `interest_id`)
);

CREATE TABLE `clients` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `company_name` varchar(150) NOT NULL,
  `email` varchar(255) UNIQUE NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(20),
  `address` varchar(255),
  `contact_person` varchar(100),
  `company_details` text,
  `created_at` timestamp DEFAULT (now()),
  `updated_at` timestamp
);

CREATE TABLE `internships` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `description` text,
  `client_id` int NOT NULL,
  `contact_phone` varchar(30),
  `contact_email` varchar(255),
  `start_date` date,
  `end_date` date,
  `seats` int,
  `created_at` timestamp DEFAULT (now()),
  `updated_at` timestamp
);

ALTER TABLE `student_interests` ADD FOREIGN KEY (`student_id`) REFERENCES `students` (`id`);

ALTER TABLE `student_interests` ADD FOREIGN KEY (`interest_id`) REFERENCES `interests` (`id`);

ALTER TABLE `internships` ADD FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`);
