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
				// Handle the response from the server
				if (response.ok) {
					// Login successful
					console.log('Login successful');
					// Redirect to the index.html page
					window.location.href = 'index.html';
				} else {
					// Login failed
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

	setSlideNav();
	setHeaderBackground();

	$('#scrollToContent').click(function(e) {
		e.preventDefault();
		$.scrollTo("#portfolio", 1000, {
			offset: -($('#header .top').height()),
			axis: 'y'
		});
	});

	$('nav > ul > li > a').click(function(e) {
		e.preventDefault();
		$.scrollTo($(this).attr('href'), 400, {
			offset: -($('#header .top').height()),
			axis: 'y'
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
		registerUser(e);
	});
});