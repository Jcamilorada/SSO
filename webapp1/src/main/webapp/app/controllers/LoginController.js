angular.module('login').controller('LoginController', function($resource){
   var vm = this;

    var source = $resource("userInfo");
    var data = source.get({}, function(){
        vm.user = data;
    });

    vm.text = "hola 1";
});