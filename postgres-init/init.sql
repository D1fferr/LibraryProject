create table if not exists public.books
(
    book_id          uuid    not null
        primary key,
    book_author      varchar not null,
    book_year        integer not null,
    book_publication varchar,
    book_language    varchar not null,
    book_items       integer,
    book_image       varchar,
    book_genre       varchar not null,
    book_added_at    timestamp,
    book_name        varchar
);

INSERT INTO public.books (book_id, book_author, book_year, book_publication, book_language, book_items, book_image, book_genre, book_added_at, book_name) VALUES
                                                                                                                                                              ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a01', 'George Orwell', 1949, 'Secker & Warburg', 'English', 12, '/api/images/a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a01.jpeg', 'Fiction', NOW(), 'Nineteen Eighty-Four'),
                                                                                                                                                              ('b1ffccaa-8d1c-4ef9-bb7e-7cc9ce491b02', 'Jane Austen', 1813, 'T. Egerton', 'English', 8, '/api/images/b1ffccaa-8d1c-4ef9-bb7e-7cc9ce491b02.jpeg', 'Fiction', NOW(), 'Pride and Prejudice'),
                                                                                                                                                              ('c2ddbb11-7e2d-4af8-aa7f-6bb9bd380a03', 'Yuval Noah Harari', 2011, 'Harvill Secker', 'English', 15, '/api/images/c2ddbb11-7e2d-4af8-aa7f-6bb9bd380a03.jpeg', 'Non-Fiction', NOW(), 'Sapiens: A Brief History of Humankind'),
                                                                                                                                                              ('d3eccc22-6f3e-4bf9-bb8e-7cc9ce491b04', 'J.K. Rowling', 1997, 'Bloomsbury', 'English', 20, '/api/images/d3eccc22-6f3e-4bf9-bb8e-7cc9ce491b04.jpeg', 'Fantasy', NOW(), 'Harry Potter and the Philosopher''s Stone'),
                                                                                                                                                              ('e4fddd33-5e4f-4cf0-cc9f-8dd0df502c05', 'J.R.R. Tolkien', 1954, 'George Allen & Unwin', 'English', 10, '/api/images/e4fddd33-5e4f-4cf0-cc9f-8dd0df502c05.jpeg', 'Fantasy', NOW(), 'The Fellowship of the Ring'),
                                                                                                                                                              ('f5aeee44-4f5f-4df1-dd10-9ee1ef613d06', 'Harper Lee', 1960, 'J.B. Lippincott & Co.', 'English', 7, '/api/images/f5aeee44-4f5f-4df1-dd10-9ee1ef613d06.jpeg', 'Fiction', NOW(), 'To Kill a Mockingbird'),
                                                                                                                                                              ('a6bfff55-3f6f-4ef2-ee21-0ff2ff714e07', 'Michio Kaku', 2018, 'Anchor Books', 'English', 5, '/api/images/a6bfff55-3f6f-4ef2-ee21-0ff2ff714e07.jpeg', 'Science Fiction', NOW(), 'The Future of Humanity'),
                                                                                                                                                              ('b7c00066-2f7f-4ff3-ff32-1ff3ff825f08', 'Agatha Christie', 1934, 'Collins Crime Club', 'English', 14, '/api/images/b7c00066-2f7f-4ff3-ff32-1ff3ff825f08.jpeg', 'Mystery', NOW(), 'Murder on the Orient Express'),
                                                                                                                                                              ('c8d11177-1f8f-4ff4-aa43-2ff4ff936f09', 'Stephen Hawking', 1988, 'Bantam Books', 'English', 9, '/api/images/c8d11177-1f8f-4ff4-aa43-2ff4ff936f09.jpeg', 'Non-Fiction', NOW(), 'A Brief History of Time'),
                                                                                                                                                              ('d9e22288-0f9f-4ff5-bb54-3ff5ffa47e0a', 'Dr. Seuss', 1957, 'Random House', 'English', 18, '/api/images/d9e22288-0f9f-4ff5-bb54-3ff5ffa47e0a.jpeg', 'Childrens', NOW(), 'The Cat in the Hat'),
                                                                                                                                                              ('e0f33399-9e9f-4ff6-cc65-4ff6ffb58f0b', 'Emily Brontë', 1847, 'Thomas Cautley Newby', 'English', 6, '/api/images/e0f33399-9e9f-4ff6-cc65-4ff6ffb58f0b.jpeg', 'Romance', NOW(), 'Wuthering Heights'),
                                                                                                                                                              ('f1f444aa-8e9f-4ff7-dd76-5ff7ffc69f0c', 'Dan Brown', 2003, 'Doubleday', 'English', 11, '/api/images/f1f444aa-8e9f-4ff7-dd76-5ff7ffc69f0c.jpeg', 'Thriller', NOW(), 'The Da Vinci Code'),
                                                                                                                                                              ('a2b555bb-7e9f-4ff8-ee87-6ff8ffd77a0d', 'Walter Isaacson', 2011, 'Simon & Schuster', 'English', 4, '/api/images/a2b555bb-7e9f-4ff8-ee87-6ff8ffd77a0d.jpeg', 'Biography', NOW(), 'Steve Jobs'),
                                                                                                                                                              ('b3c666cc-6e9f-4ff9-ff98-7ff9ffe88b0e', 'Frank Herbert', 1965, 'Chilton Books', 'English', 13, '/api/images/b3c666cc-6e9f-4ff9-ff98-7ff9ffe88b0e.jpeg', 'Science Fiction', NOW(), 'Dune'),
                                                                                                                                                              ('c4d777dd-5e9f-4ff0-aa09-8ff0fff99c0f', 'C.S. Lewis', 1950, 'Geoffrey Bles', 'English', 16, '/api/images/c4d777dd-5e9f-4ff0-aa09-8ff0fff99c0f.jpeg', 'Fantasy', NOW(), 'The Lion, the Witch and the Wardrobe'),
                                                                                                                                                              ('d5e888ee-4e9f-4ff1-bb10-9ff1fffa0d10', 'Arthur Conan Doyle', 1892, 'George Newnes', 'English', 8, '/api/images/d5e888ee-4e9f-4ff1-bb10-9ff1fffa0d10.jpeg', 'Mystery', NOW(), 'The Adventures of Sherlock Holmes'),
                                                                                                                                                              ('e6f999ff-3e9f-4ff2-cc21-0ff2fffb1e11', 'Mark Manson', 2016, 'HarperOne', 'English', 12, '/api/images/e6f999ff-3e9f-4ff2-cc21-0ff2fffb1e11.jpeg', 'Non-Fiction', NOW(), 'The Subtle Art of Not Giving a F*ck'),
                                                                                                                                                              ('f7a000aa-2e9f-4ff3-dd32-1ff3fffc2f12', 'Lewis Carroll', 1865, 'Macmillan', 'English', 10, '/api/images/f7a000aa-2e9f-4ff3-dd32-1ff3fffc2f12.jpeg', 'Childrens', NOW(), 'Alice''s Adventures in Wonderland'),
                                                                                                                                                              ('a8b111bb-1e9f-4ff4-ee43-2ff4fffd3a13', 'John Green', 2012, 'Dutton Books', 'English', 7, '/api/images/a8b111bb-1e9f-4ff4-ee43-2ff4fffd3a13.jpeg', 'Romance', NOW(), 'The Fault in Our Stars'),
                                                                                                                                                              ('b9c222cc-0e9f-4ff5-ff54-3ff5fffe4b14', 'Malcolm Gladwell', 2005, 'Little, Brown and Company', 'English', 9, '/api/images/b9c222cc-0e9f-4ff5-ff54-3ff5fffe4b14.jpeg', 'Non-Fiction', NOW(), 'Blink: The Power of Thinking Without Thinking'),
                                                                                                                                                              ('c0d333dd-9d9f-4ff6-aa65-4ff6ffff5c15', 'Ray Bradbury', 1953, 'Ballantine Books', 'English', 11, '/api/images/c0d333dd-9d9f-4ff6-aa65-4ff6ffff5c15.jpeg', 'Science Fiction', NOW(), 'Fahrenheit 451'),
                                                                                                                                                              ('d1e444ee-8d9f-4ff7-bb76-5ff7aaaa6d16', 'Gillian Flynn', 2012, 'Crown Publishing Group', 'English', 14, '/api/images/d1e444ee-8d9f-4ff7-bb76-5ff7aaaa6d16.jpeg', 'Thriller', NOW(), 'Gone Girl'),
                                                                                                                                                              ('e2f555ff-7d9f-4ff8-cc87-6ff8bbbb7e17', 'J.R.R. Tolkien', 1937, 'George Allen & Unwin', 'English', 17, '/api/images/e2f555ff-7d9f-4ff8-cc87-6ff8bbbb7e17.jpeg', 'Fantasy', NOW(), 'The Hobbit'),
                                                                                                                                                              ('f3a666aa-6d9f-4ff9-dd98-7ff9cccc8f18', 'Anne Frank', 1947, 'Contact Publishing', 'English', 5, '/api/images/f3a666aa-6d9f-4ff9-dd98-7ff9cccc8f18.jpeg', 'Biography', NOW(), 'The Diary of a Young Girl'),
                                                                                                                                                              ('a4b777bb-5d9f-4ff0-ee09-8ff0dddd9a19', 'E.L. James', 2011, 'Vintage Books', 'English', 20, '/api/images/a4b777bb-5d9f-4ff0-ee09-8ff0dddd9a19.jpeg', 'Romance', NOW(), 'Fifty Shades of Grey'),
                                                                                                                                                              ('b5c888cc-4d9f-4ff1-ff10-9ff1eeee0b20', 'Robert Kiyosaki', 1997, 'TechPress', 'English', 13, '/api/images/b5c888cc-4d9f-4ff1-ff10-9ff1eeee0b20.jpeg', 'Non-Fiction', NOW(), 'Rich Dad Poor Dad'),
                                                                                                                                                              ('c6d999dd-3d9f-4ff2-aa21-0ff2ffff1c21', 'Dav Pilkey', 1997, 'Scholastic', 'English', 19, '/api/images/c6d999dd-3d9f-4ff2-aa21-0ff2ffff1c21.jpeg', 'Childrens', NOW(), 'Captain Underpants'),
                                                                                                                                                              ('d7e000ee-2d9f-4ff3-bb32-1ff3aaaa2d22', 'Stephen King', 1977, 'Doubleday', 'English', 8, '/api/images/d7e000ee-2d9f-4ff3-bb32-1ff3aaaa2d22.jpeg', 'Thriller', NOW(), 'The Shining'),
                                                                                                                                                              ('e8f111ff-1d9f-4ff4-cc43-2ff4bbbb3e23', 'Isaac Asimov', 1951, 'Gnome Press', 'English', 6, '/api/images/e8f111ff-1d9f-4ff4-cc43-2ff4bbbb3e23.jpeg', 'Science Fiction', NOW(), 'Foundation'),
                                                                                                                                                              ('f9a222aa-0d9f-4ff5-dd54-3ff5cccc4f24', 'Maurice Sendak', 1963, 'Harper & Row', 'English', 12, '/api/images/f9a222aa-0d9f-4ff5-dd54-3ff5cccc4f24.jpeg', 'Childrens', NOW(), 'Where the Wild Things Are');


create table if not exists public.expected_book
(
    expected_book_id          uuid                                             not null
        primary key,
    expected_book_name        varchar                                          not null,
    expected_book_year        integer                                          not null,
    expected_book_publication varchar                                          not null,
    expected_book_language    varchar                                          not null,
    expected_book_image       varchar,
    expected_book_genre       varchar                                          not null,
    expected_book_added_at    timestamp                                        not null,
    expected_book_author      varchar                                          not null,
    expected_book_pieces      integer                                          not null,
    expected_book_status      varchar(50) default 'CREATED'::character varying not null
);

INSERT INTO public.expected_book (expected_book_id, expected_book_name, expected_book_year, expected_book_publication, expected_book_language, expected_book_image, expected_book_genre, expected_book_added_at, expected_book_author, expected_book_pieces, expected_book_status) VALUES
                                                                                                                                                                                                                                                                                       ('a1b2c3d4-e5f6-4ab8-9abc-ef1234567801', 'The Great Gatsby', 1925, 'Charles Scribner''s Sons', 'English', '/api/images/a1b2c3d4-e5f6-4ab8-9abc-ef1234567801.jpeg', 'Fiction', NOW(), 'F. Scott Fitzgerald', 5, 'CREATED'),
                                                                                                                                                                                                                                                                                       ('b2c3d4e5-f6a7-4bc9-9bcd-f12345678902', 'Thinking, Fast and Slow', 2011, 'Farrar, Straus and Giroux', 'English', '/api/images/b2c3d4e5-f6a7-4bc9-9bcd-f12345678902.jpeg', 'Non-Fiction', NOW(), 'Daniel Kahneman', 4, 'CREATED'),
                                                                                                                                                                                                                                                                                       ('c3d4e5f6-a7b8-4cd0-9cde-123456789a03', 'Charlotte''s Web', 1952, 'Harper & Brothers', 'English', '/api/images/c3d4e5f6-a7b8-4cd0-9cde-123456789a03.jpeg', 'Childrens', NOW(), 'E.B. White', 10, 'CREATED'),
                                                                                                                                                                                                                                                                                       ('d4e5f6a7-b8c9-4de1-9def-234567890b04', 'The Name of the Wind', 2007, 'DAW Books', 'English', '/api/images/d4e5f6a7-b8c9-4de1-9def-234567890b04.jpeg', 'Fantasy', NOW(), 'Patrick Rothfuss', 6, 'CREATED'),
                                                                                                                                                                                                                                                                                       ('e5f6a7b8-c9d0-4ef2-9ef0-345678901c05', 'The Immortal Life of Henrietta Lacks', 2010, 'Crown Publishing Group', 'English', '/api/images/e5f6a7b8-c9d0-4ef2-9ef0-345678901c05.jpeg', 'Non-Fiction', NOW(), 'Rebecca Skloot', 7, 'CREATED'),
                                                                                                                                                                                                                                                                                       ('a7b8c9d0-e1f2-4ab3-9f01-456789012d06', 'Matilda', 1988, 'Jonathan Cape', 'English', '/api/images/a7b8c9d0-e1f2-4ab3-9f01-456789012d06.jpeg', 'Childrens', NOW(), 'Roald Dahl', 12, 'CREATED'),
                                                                                                                                                                                                                                                                                       ('b8c9d0e1-f2a3-4bc4-9a12-567890123e07', 'The Martian', 2011, 'Crown Publishing Group', 'English', '/api/images/b8c9d0e1-f2a3-4bc4-9a12-567890123e07.jpeg', 'Science Fiction', NOW(), 'Andy Weir', 9, 'CREATED'),
                                                                                                                                                                                                                                                                                       ('c9d0e1f2-a3b4-4cd5-9b23-678901234f08', 'The Girl with the Dragon Tattoo', 2005, 'Norstedts Förlag', 'English', '/api/images/c9d0e1f2-a3b4-4cd5-9b23-678901234f08.jpeg', 'Thriller', NOW(), 'Stieg Larsson', 11, 'CREATED'),
                                                                                                                                                                                                                                                                                       ('d0e1f2a3-b4c5-4de6-9c34-789012345a09', 'Becoming', 2018, 'Crown Publishing Group', 'English', '/api/images/d0e1f2a3-b4c5-4de6-9c34-789012345a09.jpeg', 'Biography', NOW(), 'Michelle Obama', 8, 'CREATED'),
                                                                                                                                                                                                                                                                                       ('e1f2a3b4-c5d6-4ef7-9d45-890123456b10', 'The Secret Garden', 1911, 'Frederick A. Stokes', 'English', '/api/images/e1f2a3b4-c5d6-4ef7-9d45-890123456b10.jpeg', 'Childrens', NOW(), 'Frances Hodgson Burnett', 6, 'CREATED'),
                                                                                                                                                                                                                                                                                       ('f2a3b4c5-d6e7-4af8-9e56-901234567c11', 'The Wise Man''s Fear', 2011, 'DAW Books', 'English', '/api/images/f2a3b4c5-d6e7-4af8-9e56-901234567c11.jpeg', 'Fantasy', NOW(), 'Patrick Rothfuss', 5, 'CREATED'),
                                                                                                                                                                                                                                                                                       ('a3b4c5d6-e7f8-4ab9-9f67-012345678d12', 'Outliers: The Story of Success', 2008, 'Little, Brown and Company', 'English', '/api/images/a3b4c5d6-e7f8-4ab9-9f67-012345678d12.jpeg', 'Non-Fiction', NOW(), 'Malcolm Gladwell', 13, 'CREATED'),
                                                                                                                                                                                                                                                                                       ('b4c5d6e7-f8a9-4bc0-9a78-123456789e13', 'The BFG', 1982, 'Jonathan Cape', 'English', '/api/images/b4c5d6e7-f8a9-4bc0-9a78-123456789e13.jpeg', 'Childrens', NOW(), 'Roald Dahl', 10, 'CREATED'),
                                                                                                                                                                                                                                                                                       ('c5d6e7f8-a9b0-4cd1-9b89-234567890f14', 'Children of Time', 2015, 'Tor Books', 'English', '/api/images/c5d6e7f8-a9b0-4cd1-9b89-234567890f14.jpeg', 'Science Fiction', NOW(), 'Adrian Tchaikovsky', 4, 'CREATED'),
                                                                                                                                                                                                                                                                                       ('d6e7f8a9-b0c1-4de2-9c9a-345678901a15', 'The Silent Patient', 2019, 'Celadon Books', 'English', '/api/images/d6e7f8a9-b0c1-4de2-9c9a-345678901a15.jpeg', 'Thriller', NOW(), 'Alex Michaelides', 15, 'CREATED'),
                                                                                                                                                                                                                                                                                       ('e7f8a9b0-c1d2-4ef3-9dab-456789012b16', 'Educated', 2018, 'Random House', 'English', '/api/images/e7f8a9b0-c1d2-4ef3-9dab-456789012b16.jpeg', 'Biography', NOW(), 'Tara Westover', 9, 'CREATED'),
                                                                                                                                                                                                                                                                                       ('f8a9b0c1-d2e3-4af4-9ebc-567890123c17', 'The Chronicles of Narnia', 1950, 'Geoffrey Bles', 'English', '/api/images/f8a9b0c1-d2e3-4af4-9ebc-567890123c17.jpeg', 'Fantasy', NOW(), 'C.S. Lewis', 18, 'CREATED'),
                                                                                                                                                                                                                                                                                       ('a9b0c1d2-e3f4-4ab5-9fcd-678901234d18', 'The Power of Habit', 2012, 'Random House', 'English', '/api/images/a9b0c1d2-e3f4-4ab5-9fcd-678901234d18.jpeg', 'Non-Fiction', NOW(), 'Charles Duhigg', 7, 'CREATED'),
                                                                                                                                                                                                                                                                                       ('b0c1d2e3-f4a5-4bc6-9ade-789012345e19', 'James and the Giant Peach', 1961, 'Alfred A. Knopf', 'English', '/api/images/b0c1d2e3-f4a5-4bc6-9ade-789012345e19.jpeg', 'Childrens', NOW(), 'Roald Dahl', 11, 'CREATED'),
                                                                                                                                                                                                                                                                                       ('c1d2e3f4-a5b6-4cd7-9bef-890123456f20', 'Project Hail Mary', 2021, 'Ballantine Books', 'English', '/api/images/c1d2e3f4-a5b6-4cd7-9bef-890123456f20.jpeg', 'Science Fiction', NOW(), 'Andy Weir', 14, 'CREATED'),
                                                                                                                                                                                                                                                                                       ('d2e3f4a5-b6c7-4de8-9cf0-901234567a21', 'The Couple Next Door', 2016, 'Pamela Dorman Books', 'English', '/api/images/d2e3f4a5-b6c7-4de8-9cf0-901234567a21.jpeg', 'Thriller', NOW(), 'Shari Lapena', 8, 'CREATED'),
                                                                                                                                                                                                                                                                                       ('e3f4a5b6-c7d8-4ef9-9e01-012345678b22', 'Mistborn: The Final Empire', 2006, 'Tor Books', 'English', '/api/images/e3f4a5b6-c7d8-4ef9-9e01-012345678b22.jpeg', 'Fantasy', NOW(), 'Brandon Sanderson', 12, 'CREATED'),
                                                                                                                                                                                                                                                                                       ('a5b6c7d8-e9f0-4ab1-9f12-123456789c23', 'The Tipping Point', 2000, 'Little, Brown and Company', 'English', '/api/images/a5b6c7d8-e9f0-4ab1-9f12-123456789c23.jpeg', 'Non-Fiction', NOW(), 'Malcolm Gladwell', 5, 'CREATED'),
                                                                                                                                                                                                                                                                                       ('b6c7d8e9-f0a1-4bc2-9a23-234567890d24', 'The Wonderful Wizard of Oz', 1900, 'George M. Hill Company', 'English', '/api/images/b6c7d8e9-f0a1-4bc2-9a23-234567890d24.jpeg', 'Childrens', NOW(), 'L. Frank Baum', 9, 'CREATED'),
                                                                                                                                                                                                                                                                                       ('c7d8e9f0-a1b2-4cd3-9b34-345678901e25', 'Leviathan Wakes', 2011, 'Orbit Books', 'English', '/api/images/c7d8e9f0-a1b2-4cd3-9b34-345678901e25.jpeg', 'Science Fiction', NOW(), 'James S.A. Corey', 7, 'CREATED'),
                                                                                                                                                                                                                                                                                       ('d8e9f0a1-b2c3-4de4-9c45-456789012f26', 'The Woman in the Window', 2018, 'William Morrow', 'English', '/api/images/d8e9f0a1-b2c3-4de4-9c45-456789012f26.jpeg', 'Thriller', NOW(), 'A.J. Finn', 10, 'CREATED'),
                                                                                                                                                                                                                                                                                       ('e9f0a1b2-c3d4-4ef5-9d56-567890123a27', 'Leonardo da Vinci', 2017, 'Simon & Schuster', 'English', '/api/images/e9f0a1b2-c3d4-4ef5-9d56-567890123a27.jpeg', 'Biography', NOW(), 'Walter Isaacson', 4, 'CREATED'),
                                                                                                                                                                                                                                                                                       ('f0a1b2c3-d4e5-4af6-9e67-678901234b28', 'The Way of Kings', 2010, 'Tor Books', 'English', '/api/images/f0a1b2c3-d4e5-4af6-9e67-678901234b28.jpeg', 'Fantasy', NOW(), 'Brandon Sanderson', 16, 'CREATED');


create table if not exists public.announcement
(
    announcement_id           uuid      not null
        primary key,
    announcement_body         text      not null,
    announcement_type         varchar   not null,
    announcement_date         date      not null,
    announcement_photo        varchar,
    announcement_name         varchar   not null,
    announcement_created_date timestamp not null
);

INSERT INTO public.announcement (announcement_id, announcement_body, announcement_type, announcement_date, announcement_photo, announcement_name, announcement_created_date) VALUES
                                                                                                                                                                                 ('f1e2d3c4-b5a6-4ab0-9abc-567890abcde1', 'Join us for an interactive creative writing workshop where you will learn the fundamentals of storytelling, character development, and plot structure. Perfect for aspiring writers of all levels.', 'Workshop', '2026-05-08', '/api/images/f1e2d3c4-b5a6-4ab0-9abc-567890abcde1.jpeg', 'Creative Writing Workshop', NOW()),
                                                                                                                                                                                 ('a2b3c4d5-e6f7-4ab8-9bcd-678901bcdef2', 'Discover the rich history of illuminated manuscripts from medieval Europe. Our expert curator will present rare digital copies and explain the intricate process of manuscript creation.', 'Lecture', '2026-05-12', '/api/images/a2b3c4d5-e6f7-4ab8-9bcd-678901bcdef2.jpeg', 'Medieval Manuscripts: Art of Illumination', NOW()),
                                                                                                                                                                                 ('b3c4d5e6-f7a8-4bc9-9cde-789012cdefa3', 'This month we are reading "Project Hail Mary" by Andy Weir. Join fellow book lovers for a lively discussion about this thrilling science fiction adventure.', 'Book Club', '2026-05-15', '/api/images/b3c4d5e6-f7a8-4bc9-9cde-789012cdefa3.jpeg', 'Sci-Fi Book Club: Project Hail Mary', NOW()),
                                                                                                                                                                                 ('c4d5e6f7-a8b9-4cd0-9def-890123defaa4', 'Annual photography exhibition featuring works by local photographers inspired by libraries, books, and the joy of reading.', 'Exhibition', '2026-05-18', '/api/images/c4d5e6f7-a8b9-4cd0-9def-890123defaa4.jpeg', 'Through the Lens: Libraries in Focus', NOW()),
                                                                                                                                                                                 ('d5e6f7a8-b9c0-4de1-9ef0-901234efaaa5', 'Saturday morning storytime for children ages 3-7. Join our beloved storyteller for an hour of magical tales, songs, and crafts.', 'Reading Session', '2026-05-20', '/api/images/d5e6f7a8-b9c0-4de1-9ef0-901234efaaa5.jpeg', 'Magical Storytime for Kids', NOW()),
                                                                                                                                                                                 ('e6f7a8b9-c0d1-4ef2-9f01-012345faaaa6', 'Annual Library Conference: "Libraries in the Digital Age". Keynote speeches, panel discussions, and networking opportunities.', 'Conference', '2026-05-22', '/api/images/e6f7a8b9-c0d1-4ef2-9f01-012345faaaa6.jpeg', 'Annual Library Conference 2026', NOW()),
                                                                                                                                                                                 ('f7a8b9c0-d1e2-4af3-9a12-123456aaaaa7', 'Learn how to navigate our digital library resources including e-books, audiobooks, and research databases. Perfect for seniors.', 'Webinar', '2026-05-25', '/api/images/f7a8b9c0-d1e2-4af3-9a12-123456aaaaa7.jpeg', 'Digital Library 101: Online Resources', NOW()),
                                                                                                                                                                                 ('a8b9c0d1-e2f3-4ab4-9b23-234567aaaaa8', 'Evening of classical music and poetry. Local musicians will perform pieces inspired by famous poems.', 'Other', '2026-05-26', '/api/images/a8b9c0d1-e2f3-4ab4-9b23-234567aaaaa8.jpeg', 'Poetry & Classical Music Evening', NOW()),
                                                                                                                                                                                 ('b9c0d1e2-f3a4-4bc5-9c34-345678aaaaa9', 'Two-day intensive workshop for aspiring children''s book authors and illustrators. Limited to 20 participants.', 'Workshop', '2026-05-27', '/api/images/b9c0d1e2-f3a4-4bc5-9c34-345678aaaaa9.jpeg', 'Children''s Book Author Workshop', NOW()),
                                                                                                                                                                                 ('c0d1e2f3-a4b5-4cd6-9d45-456789aaaaaa', 'Discussion about the role of libraries in preserving local history. Learn about our archive collection and oral history projects.', 'Lecture', '2026-05-29', '/api/images/c0d1e2f3-a4b5-4cd6-9d45-456789aaaaaa.jpeg', 'Preserving Our Community History', NOW());


create table if not exists public.news
(
    news_id    uuid not null
        primary key,
    news_body  text not null,
    news_photo varchar,
    news_name  varchar,
    news_date  timestamp
);

INSERT INTO public.news (news_id, news_body, news_photo, news_name, news_date) VALUES
('d1e2f3a4-b5c6-7890-1234-567890abcdef', 'We are thrilled to announce the grand opening of our new children''s wing! The expansion includes a dedicated storytime room, interactive learning stations, and a cozy reading nook for young readers. Join us for the celebration on the first floor.', '/api/images/d1e2f3a4-b5c6-7890-1234-567890abcdef.jpeg', 'Grand Opening of Children''s Wing', '2026-03-15 10:00:00'),

('e2f3a4b5-c6d7-8901-2345-678901bcdefa', 'Our library has been awarded the "Best Community Library 2026" award by the National Library Association. This recognition celebrates our commitment to community engagement, innovative programs, and exceptional service. Thank you to all our patrons!', '/api/images/e2f3a4b5-c6d7-8901-2345-678901bcdefa.jpeg', 'Library Wins Best Community Library Award', '2026-02-10 14:30:00'),

('f3a4b5c6-d7e8-9012-3456-789012cdefaa', 'We are sad to announce the passing of our beloved head librarian, Margaret Chen, who served our community for over 35 years. A memorial service will be held in her honor. She will be deeply missed by staff and patrons alike.', '/api/images/f3a4b5c6-d7e8-9012-3456-789012cdefaa.jpeg', 'In Memoriam: Head Librarian Margaret Chen', '2026-01-20 09:00:00'),

('a4b5c6d7-e8f9-0123-4567-890123defaaa', 'New self-checkout kiosks have been installed on all three floors! Check out books quickly and easily without waiting in line. Staff are available to assist if you need help using the new system.', '/api/images/a4b5c6d7-e8f9-0123-4567-890123defaaa.jpeg', 'New Self-Checkout Kiosks Now Available', '2026-02-28 11:45:00'),

('b5c6d7e8-f9a0-1234-5678-901234efaaaa', 'Donate your gently used books during our annual book drive from March 1-31. All proceeds support literacy programs for underserved children in our community. Last year we collected over 5,000 books!', '/api/images/b5c6d7e8-f9a0-1234-5678-901234efaaaa.jpeg', 'Annual Book Drive Starts March 1', '2026-02-25 13:20:00'),

('c6d7e8f9-a0b1-2345-6789-012345faaaaa', 'Summer reading program registration opens today! Kids and teens can earn prizes for reading throughout June, July, and August. This year''s theme is "Adventure Begins at Your Library." Sign up at the front desk.', '/api/images/c6d7e8f9-a0b1-2345-6789-012345faaaaa.jpeg', 'Summer Reading Program Registration Open', '2026-03-01 08:00:00'),

('d7e8f9a0-b1c2-3456-7890-123456aaaaaa', 'Our library has partnered with the local school district to provide free homework help sessions every Tuesday and Thursday from 4-6 PM. Certified teachers and volunteer tutors will assist students in grades K-12.', '/api/images/d7e8f9a0-b1c2-3456-7890-123456aaaaaa.jpeg', 'Free Homework Help Now Available', '2026-02-18 16:00:00'),

('f9a0b1c2-d3e4-5678-9012-345678aaaaaa', 'Our popular "Books & Brews" book club is returning for spring! Meet fellow book lovers at the local coffee shop each Wednesday evening. March selection: "The Midnight Library" by Matt Haig.', '/api/images/f9a0b1c2-d3e4-5678-9012-345678aaaaaa.jpeg', 'Books & Brews Book Club Returns', '2026-03-05 18:30:00'),

('a0b1c2d3-e4f5-6789-0123-456789aaaaaa', 'We are upgrading our online catalog system from March 20-22. During this time, the catalog may be temporarily unavailable. We apologize for the inconvenience and appreciate your patience.', '/api/images/a0b1c2d3-e4f5-6789-0123-456789aaaaaa.jpeg', 'Scheduled Catalog Upgrade March 20-22', '2026-03-18 09:15:00');

create table if not exists public.auth_user
(
    id            uuid    not null
        primary key,
    user_name     varchar not null
        unique,
    user_password varchar not null,
    user_role     varchar not null,
    user_email    varchar not null
);


create table if not exists public.most_popular_books_counter
(
    id        uuid    not null
        primary key,
    book_id   uuid    not null,
    crated_at timestamp,
    counter   integer not null
);


create table if not exists public.person
(
    user_id           uuid    not null
        primary key,
    user_name         varchar not null
        unique,
    user_email        varchar not null
        unique,
    user_library_code varchar
);


create table if not exists public.report_availability
(
    id               uuid                                         not null
        primary key,
    user_id          uuid
                                                                  references public.person
                                                                      on delete set null,
    expected_book_id uuid
                                                                  references public.expected_book
                                                                      on delete set null,
    user_email       varchar
                                                                  references public.person (user_email)
                                                                      on delete set null,
    status           varchar default 'CREATED'::character varying not null,
    username         varchar                                      not null
);

create table if not exists public.report_availability_errors
(
    id               uuid                                         not null
        primary key,
    user_id          uuid
                                                                  references public.person
                                                                      on delete set null,
    expected_book_id uuid
                                                                  references public.expected_book
                                                                      on delete set null,
    user_email       varchar
                                                                  references public.person (user_email)
                                                                      on delete set null,
    status           varchar default 'CREATED'::character varying not null,
    error            varchar                                      not null,
    username         varchar                                      not null
);

create table if not exists public.reservation
(
    reservation_id        uuid      not null
        primary key,
    reservation_date      date      not null,
    created_at            timestamp not null,
    reservation_book_id   uuid
                                    references public.books
                                        on delete set null,
    reservation_person_id uuid
                                    references public.person
                                        on delete set null,
    reservation_status    varchar default 'CREATED'::character varying
);


create table if not exists public.reservation_cancellation_notifications
(
    id             uuid    not null
        primary key,
    reservation_id uuid    not null,
    message        varchar not null
);



