insert into public.roles (name)
values ('ADMIN'),
       ('INSTRUCTOR');

insert into public.users (username, password, full_name, email, phone_number, image, active, role_id)
values ('admin', '$2a$10$GpFenGLx8wUlhDULKOf9A.ho8XHzt1CoVxh3/1ZSgWCmnLx/Yl656', 'ADMIN USER', 'admin@fsoft.com',
        null, null, true, 1),
       ('instructor1', '$2a$10$GpFenGLx8wUlhDULKOf9A.ho8XHzt1CoVxh3/1ZSgWCmnLx/Yl656', 'John Doe', 'john.doe@gmail.com', '0123456789', null, true, 2),
       ('guest1', '$2a$10$GpFenGLx8wUlhDULKOf9A.ho8XHzt1CoVxh3/1ZSgWCmnLx/Yl656', 'Mary Jane', 'mary.jane@gmail.com', '0123456789', null, true, null);

insert into public.categories (id, name, slug, image)
values ('Development', 'development', 'https://res.cloudinary.com/fpt-software-quiz/image/upload/v1644660774/developement_zah93x.jpg'),
       ('Business', 'business', 'https://res.cloudinary.com/fpt-software-quiz/image/upload/v1644660774/business_niyc4b.jpg'),
       ('Design', 'design', 'https://res.cloudinary.com/fpt-software-quiz/image/upload/v1644660774/design_k3sin6.jpg'),
       ('IT and Software', 'it-and-software', 'https://res.cloudinary.com/fpt-software-quiz/image/upload/v1644660774/it-and-software_zguif9.jpg'),
       ('Marketing', 'marketing', 'https://res.cloudinary.com/fpt-software-quiz/image/upload/v1644660774/marketing_bliev0.jpg'),
       ('Music', 'music', 'https://res.cloudinary.com/fpt-software-quiz/image/upload/v1644660774/music_t4mncd.jpg'),
       ('Personal Development', 'personal-development', 'https://res.cloudinary.com/fpt-software-quiz/image/upload/v1644660774/personal-developement_npjujq.jpg'),
       ('Photography', 'photography', 'https://res.cloudinary.com/fpt-software-quiz/image/upload/v1644660774/photography_kdazmf.jpg');

insert into public.difficulties (level, point)
values ('EASY', 0),
       ('MEDIUM', 1),
       ('HARD', 2);

insert into public.tags (name)
values ('HTML5/CSS3'),
       ('Javascript'),
       ('Java'),
       ('Python'),
       ('C/C++'),
       ('C#'),
       ('DevOps');

insert into public.quizzes (title, description, status, image, category_id, instructor_id)
values ('Elementary HTML/CSS for beginner', 'Basic HTML/CSS for...', 'PUBLIC',
        'https://res.cloudinary.com/fpt-software-quiz/image/upload/v1644700398/html-css-course_n8stxa.jpg', 1, 2);

insert into questions (title, is_multiple, difficulty_id, quiz_id, tag_id)
values ('What does CSS stand for?', false, 1, 1, 1),
       ('Which CSS property is used to change the text color of an element?', false, 1, 1, 1);

insert into answers (text, is_correct, question_id)
values ('Creative Style Sheet', false, 1),
       ('Cascading Style Sheet', true, 1),
       ('Colorful Style Sheet', false, 1),
       ('Computer Style Sheet', false, 1),
       ('color', true, 2),
       ('text-colort', false, 2),
       ('fg-color', false, 2);