var express = require('express');
var cookieParser = require('cookie-parser');
var router = express.Router();
router.use(cookieParser());

router.get('/',function(req,res){
    res.render('index.html');
 });

router.get('/register', function(req, res) {
    res.render('register.html');
})
router.get('/main_page', function(req, res) {
    if (req.cookies.token != null) {
        res.render('main_page.html');
        console.log(req.cookies);
    }
    else res.render('index.html');
})

router.get('/light_status', function(req, res) {
    if (req.cookies.token != null) {
        res.render('light_status.html');
    }
    else res.render("main_page.html");
})
router.get('/light_custom', function(req, res) {
    if (req.cookies.token != null) {
        res.render('light_custom.html');
    }
    else res.render("main_page.html");
})
router.get('/visit_log', function(req, res) {
    if (req.cookies.token != null) {
        res.render('visit_log.html');
    }
    else res.render("main_page.html");
})
router.get('/modify_user', function(req, res) {
    if (req.cookies.token != null) {
        res.render('modify_user.html');
    }
    else res.render("main_page.html");
})

module.exports = router;