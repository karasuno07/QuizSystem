insert into public.roles (id, name)
values (1, 'ADMIN'),
       (2, 'INSTRUCTOR');

insert into public.users (id, username, password, full_name, email, phone_number, image, active, role_id)
values (1, 'admin', '$2a$10$GpFenGLx8wUlhDULKOf9A.ho8XHzt1CoVxh3/1ZSgWCmnLx/Yl656', 'ADMIN USER', 'admin@fsoft.com',
        null, null, true, 1),
       (2, 'instructor1', '$2a$10$GpFenGLx8wUlhDULKOf9A.ho8XHzt1CoVxh3/1ZSgWCmnLx/Yl656', 'John Doe', 'john.doe@gmail.com', '0123456789', null, true, 2),
       (3, 'guest1', '$2a$10$GpFenGLx8wUlhDULKOf9A.ho8XHzt1CoVxh3/1ZSgWCmnLx/Yl656', 'Mary Jane', 'mary.jane@gmail.com', '0123456789', null, true, null);

insert into public.categories (id, name, slug, image)
values (1, 'Development', 'development', 'https://res.cloudinary.com/fpt-software-quiz/image/upload/v1644660774/developement_zah93x.jpg'),
       (2, 'Business', 'business', 'https://res.cloudinary.com/fpt-software-quiz/image/upload/v1644660774/business_niyc4b.jpg'),
       (3, 'Design', 'design', 'https://res.cloudinary.com/fpt-software-quiz/image/upload/v1644660774/design_k3sin6.jpg'),
       (4, 'IT and Software', 'it-and-software', 'https://res.cloudinary.com/fpt-software-quiz/image/upload/v1644660774/it-and-software_zguif9.jpg'),
       (5, 'Marketing', 'marketing', 'https://res.cloudinary.com/fpt-software-quiz/image/upload/v1644660774/marketing_bliev0.jpg'),
       (6, 'Music', 'music', 'https://res.cloudinary.com/fpt-software-quiz/image/upload/v1644660774/music_t4mncd.jpg'),
       (7, 'Personal Development', 'personal-development', 'https://res.cloudinary.com/fpt-software-quiz/image/upload/v1644660774/personal-developement_npjujq.jpg'),
       (8, 'Photography', 'photography', 'https://res.cloudinary.com/fpt-software-quiz/image/upload/v1644660774/photography_kdazmf.jpg');

insert into public.difficulties (id, level, point)
values (1, 'EASY', 0),
       (2, 'MEDIUM', 1),
       (3, 'HARD', 2);

insert into public.tags (id, name)
values (1, 'HTML5/CSS3'),
       (2, 'Javascript'),
       (3, 'Java'),
       (4, 'Python'),
       (5, 'C/C++'),
       (6, 'C#'),
       (7, 'DevOps');

insert into public.quizzes (id, title, description, status, image, category_id, instructor_id)
values (1, 'Elementary HTML/CSS for beginner', 'Basic HTML/CSS for...', 'PUBLIC',
        'https://res.cloudinary.com/fpt-software-quiz/image/upload/v1644700398/html-css-course_n8stxa.jpg', 1, 2);

insert into questions (id, title, is_multiple, difficulty_id, quiz_id, tag_id)
values (1, 'What does CSS stand for?', false, 1, 1, 1),
       (2, 'Which CSS property is used to change the text color of an element?', false, 1, 1, 1);

insert into answers (id, text, is_correct, question_id)
values (1, 'Creative Style Sheet', false, 1),
       (2, 'Cascading Style Sheet', true, 1),
       (3, 'Colorful Style Sheet', false, 1),
       (4, 'Computer Style Sheet', false, 1),
       (5, 'color', true, 2),
       (6, 'text-colort', false, 2),
       (7, 'fg-color', false, 2);