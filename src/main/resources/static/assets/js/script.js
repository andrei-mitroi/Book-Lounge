$(document).ready(function() {
    var $wrapper = $('#wrapper');
    var $drawerRight = $('#drawer-right');

    function setSlideNav() {
        $(".toggleDrawer").click(function(e) {
            e.preventDefault();

            if ($wrapper.css('marginLeft') === '0px') {
                $drawerRight.animate({
                    marginRight: 0
                }, 500);
                $wrapper.animate({
                    marginLeft: -300
                }, 500);
            } else {
                $drawerRight.animate({
                    marginRight: -300
                }, 500);
                $wrapper.animate({
                    marginLeft: 0
                }, 500);
            }
        });
    }

    function setHeaderBackground() {
        var scrollTop = $(window).scrollTop();

        if (scrollTop > 300 || $(window).width() < 700) {
            $('#header .top').addClass('solid');
        } else {
            $('#header .top').removeClass('solid');
        }
    }

    function login() {
        var email = $('#email').val();
        var password = $('#password').val();

        fetch('/BookLounge/v1/authenticate', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                email: email,
                password: password
            })
        })
            .then(function(response) {
                if (response.ok) {
                    console.log('Login successful');
                    return response.json().then(function(data) {
                        var token = data.token;
                        var hasUploadedBook = data.hasUploadedBook;

                        if (token) {
                            sessionStorage.setItem('jwtToken', token);
                            sessionStorage.setItem('hasUploadedBook', hasUploadedBook);
                            window.location.href = 'index.html';
                        } else {
                            console.log('Token not found in the response data');
                        }
                    });
                } else {
                    console.log('Login failed');
                }
            })
            .catch(function(error) {
                console.log('An error occurred:', error);
            });
    }

    function register() {
        window.location.href = 'register.html';
    }

    function registerUser(e) {
        var currentPage = window.location.pathname;
        if (currentPage.includes('register.html')) {
            e.preventDefault();
            var firstName = $('#firstName').val();
            var lastName = $('#lastName').val();
            var email = $('#email').val();
            var password = $('#password').val();
            var role = $('#role').val();

            fetch('/BookLounge/v1/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    firstName: firstName,
                    lastName: lastName,
                    email: email,
                    password: password,
                    role: role
                })
            })
                .then(function (response) {
                    if (response.ok) {
                        console.log('Registration successful');
                        window.location.href = 'login.html';
                    } else {
                        console.log('Registration failed');
                    }
                })
                .catch(function (error) {
                    console.log('An error occurred:', error);
                });
        }
    }

    function sendEmail() {
        var name = $('#subject').val();
        var email = $('#exampleInputEmail2').val();
        var message = $('#message').val();

        $.ajax({
            type: 'POST',
            url: '/BookLounge/v1/contact',
            data: JSON.stringify({ name: name, email: email, message: message }),
            contentType: 'application/json',
            success: function(response) {
                alert(response);
                $('#subject').val('');
                $('#exampleInputEmail2').val('');
                $('#message').val('');
            },
            error: function(xhr, status, error) {
                alert('Error: ' + error);
            }
        });
    }

    function downloadWarcraft() {
        var bookId = '6478a60588457c18a979b67c';
        var token = sessionStorage.getItem('jwtToken');
        var hasUploadedBook = sessionStorage.getItem('hasUploadedBook');


        if (hasUploadedBook === 'true') {
            fetch('/BookLounge/v1/getBook/' + bookId, {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + token
                },
            })
                .then(function (response) {
                    if (response.ok) {
                        return response.blob();
                    } else {
                        throw new Error('Failed to download the book');
                    }
                })
                .then(function (blob) {
                    var link = document.createElement('a');
                    link.href = window.URL.createObjectURL(blob);
                    link.download = 'The Art of Wrath of the Lich King.pdf';
                    link.click();
                    sessionStorage.setItem('hasUploadedBook', 'false');
                })
                .catch(function (error) {
                    console.error('Failed to download the book:', error);
                });
        } else {
            console.log('User has not uploaded a book');
            $('#uploadBookPopup').addClass('show');
        }
    }


    function downloadDune() {
        var bookId = '6470dad230f3bf6c28ebeb4f';
        var token = sessionStorage.getItem('jwtToken');
        var hasUploadedBook = sessionStorage.getItem('hasUploadedBook');


        if (hasUploadedBook === 'true') {
            fetch('/BookLounge/v1/getBook/' + bookId, {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + token
                },
            })
                .then(function (response) {
                    if (response.ok) {
                        return response.blob();
                    } else {
                        throw new Error('Failed to download the book');
                    }
                })
                .then(function (blob) {
                    var link = document.createElement('a');
                    link.href = window.URL.createObjectURL(blob);
                    link.download = 'Dune.pdf';
                    link.click();
                    sessionStorage.setItem('hasUploadedBook', 'false');
                })
                .catch(function (error) {
                    console.error('Failed to download the book:', error);
                });
        } else {
            console.log('User has not uploaded a book');
            $('#uploadBookPopup').addClass('show');
        }
    }

    function downloadHarryPotter() {
        var bookId = '64709a50f887ed5eb04dc688';
        var token = sessionStorage.getItem('jwtToken');
        var hasUploadedBook = sessionStorage.getItem('hasUploadedBook');


        if (hasUploadedBook === 'true') {
            fetch('/BookLounge/v1/getBook/' + bookId, {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + token
                },
            })
                .then(function (response) {
                    if (response.ok) {
                        return response.blob();
                    } else {
                        throw new Error('Failed to download the book');
                    }
                })
                .then(function (blob) {
                    var link = document.createElement('a');
                    link.href = window.URL.createObjectURL(blob);
                    link.download = 'Harry Potter and the Sorcerer Stone.pdf';
                    link.click();
                    sessionStorage.setItem('hasUploadedBook', 'false');
                })
                .catch(function (error) {
                    console.error('Failed to download the book:', error);
                });
        } else {
            console.log('User has not uploaded a book');
            $('#uploadBookPopup').addClass('show');
        }
    }

    function downloadCooking() {
        var bookId = '6475b26c624bab17585739dd';
        var token = sessionStorage.getItem('jwtToken');
        var hasUploadedBook = sessionStorage.getItem('hasUploadedBook');


        if (hasUploadedBook === 'true') {
            fetch('/BookLounge/v1/getBook/' + bookId, {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + token
                },
            })
                .then(function (response) {
                    if (response.ok) {
                        return response.blob();
                    } else {
                        throw new Error('Failed to download the book');
                    }
                })
                .then(function (blob) {
                    var link = document.createElement('a');
                    link.href = window.URL.createObjectURL(blob);
                    link.download = 'Easy Peasy Cookbook.pdf';
                    link.click();
                    sessionStorage.setItem('hasUploadedBook', 'false');
                })
                .catch(function (error) {
                    console.error('Failed to download the book:', error);
                });
        } else {
            console.log('User has not uploaded a book');
            $('#uploadBookPopup').addClass('show');
        }
    }

    function downloadRings() {
        var bookId = '6479db26725732017a3726b0';
        var token = sessionStorage.getItem('jwtToken');
        var hasUploadedBook = sessionStorage.getItem('hasUploadedBook');


        if (hasUploadedBook === 'true') {
            fetch('/BookLounge/v1/getBook/' + bookId, {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + token
                },
            })
                .then(function (response) {
                    if (response.ok) {
                        return response.blob();
                    } else {
                        throw new Error('Failed to download the book');
                    }
                })
                .then(function (blob) {
                    var link = document.createElement('a');
                    link.href = window.URL.createObjectURL(blob);
                    link.download = 'The Lord of the Rings The Fellowship of the Ring.pdf';
                    link.click();
                    sessionStorage.setItem('hasUploadedBook', 'false');
                })
                .catch(function (error) {
                    console.error('Failed to download the book:', error);
                });
        } else {
            console.log('User has not uploaded a book');
            $('#uploadBookPopup').addClass('show');
        }
    }

    function downloadCode() {
        var bookId = '6479daa5725732017a3726af';
        var token = sessionStorage.getItem('jwtToken');
        var hasUploadedBook = sessionStorage.getItem('hasUploadedBook');


        if (hasUploadedBook === 'true') {
            fetch('/BookLounge/v1/getBook/' + bookId, {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + token
                },
            })
                .then(function (response) {
                    if (response.ok) {
                        return response.blob();
                    } else {
                        throw new Error('Failed to download the book');
                    }
                })
                .then(function (blob) {
                    var link = document.createElement('a');
                    link.href = window.URL.createObjectURL(blob);
                    link.download = 'Clean Code.pdf';
                    link.click();
                    sessionStorage.setItem('hasUploadedBook', 'false');
                })
                .catch(function (error) {
                    console.error('Failed to download the book:', error);
                });
        } else {
            console.log('User has not uploaded a book');
            $('#uploadBookPopup').addClass('show');
        }
    }

    document.addEventListener('DOMContentLoaded', function() {
        var token = sessionStorage.getItem('jwtToken');
        var hasUploadedBook = sessionStorage.getItem('hasUploadedBook');
        var uploadForm = document.getElementById('uploadForm');

        uploadForm.addEventListener('submit', function(e) {
            e.preventDefault();
            console.log('Authorization header:', 'Bearer ' + token);
            if (hasUploadedBook === 'false') {
                var formData = new FormData(uploadForm);

                fetch('/BookLounge/v1/addBook', {
                    method: 'POST',
                    headers: {
                        'Authorization': 'Bearer ' + token
                    },
                    body: formData
                })
                    .then(response => {
                        if (response.ok) {
                            return response.json();
                        } else {
                            throw new Error('Failed to add book');
                        }
                    })
                    .then(data => {
                        console.log(data);
                    })
                    .catch(error => {
                        console.error(error);
                    });
            }
        });
    });

    setSlideNav();
    setHeaderBackground();

    $('nav > ul > li > a').click(function(e) {
        e.preventDefault();
        $.scrollTo($(this).attr('href'), 400, {
            offset: -($('#header .top').height()),
            axis: 'y'
        });
    });

    $(document).ready(function() {
        $('#scrollToContent').click(function(e) {
            e.preventDefault();
            document.getElementById("Books").scrollIntoView({
                behavior: "smooth",
                block: "start"
            });
        });
    });

    $(window).scroll(function() {
        setHeaderBackground();
    });

    $('#do-login').click(function(e) {
        e.preventDefault();
        login();
    });

    $('#do-register').click(function(e) {
        e.preventDefault();
        register();
    });

    $('#do-register').click(function(e) {
        e.preventDefault();
        registerUser(e);
    });

    $('#downloadButtonWarcraft').click(function (e) {
        var hasUploadedBook = sessionStorage.getItem('hasUploadedBook');
        e.preventDefault();
        if (hasUploadedBook === 'false') {
            console.log('User has not uploaded a book');
            $('#uploadBookPopup').addClass('show');
        } else {
            downloadWarcraft();
        }
    });

    $('#downloadButtonDune').click(function (e) {
        var hasUploadedBook = sessionStorage.getItem('hasUploadedBook');
        e.preventDefault();
        if (hasUploadedBook === 'false') {
            console.log('User has not uploaded a book');
            $('#uploadBookPopup').addClass('show');
        } else {
            downloadDune();
        }
    });

    $('#downloadButtonHarryPotter').click(function (e) {
        var hasUploadedBook = sessionStorage.getItem('hasUploadedBook');
        e.preventDefault();
        if (hasUploadedBook === 'false') {
            console.log('User has not uploaded a book');
            $('#uploadBookPopup').addClass('show');
        } else {
            downloadHarryPotter();
        }
    });

    $('#downloadButtonCooking').click(function (e) {
        var hasUploadedBook = sessionStorage.getItem('hasUploadedBook');
        e.preventDefault();
        if (hasUploadedBook === 'false') {
            console.log('User has not uploaded a book');
            $('#uploadBookPopup').addClass('show');
        } else {
            downloadCooking();
        }
    });

    $('#downloadButtonRings').click(function (e) {
        var hasUploadedBook = sessionStorage.getItem('hasUploadedBook');
        e.preventDefault();
        if (hasUploadedBook === 'false') {
            console.log('User has not uploaded a book');
            $('#uploadBookPopup').addClass('show');
        } else {
            downloadRings();
        }
    });

    $('#downloadButtonCode').click(function (e) {
        var hasUploadedBook = sessionStorage.getItem('hasUploadedBook');
        e.preventDefault();
        if (hasUploadedBook === 'false') {
            console.log('User has not uploaded a book');
            $('#uploadBookPopup').addClass('show');
        } else {
            downloadCode();
        }
    });

    $('#contactForm').submit(function(e) {
        e.preventDefault();
        sendEmail();
    });

    document.addEventListener('DOMContentLoaded', function() {
        // Get the "Upload Book" button element by its ID
        var uploadButton = document.getElementById('uploadBookButton');

        // Add a click event listener to the button
        uploadButton.addEventListener('click', function() {
            redirectToUploadPage();
        });

        // Function to redirect the user to the upload.html page
        function redirectToUploadPage() {
            window.location.href = 'uploadBook.html';
        }
    });
});
