
  $(document).ready(function(){
    $('input.timepicker').timepicker({
    timeFormat: 'HH:mm',
    interval: 30,
    //timeFormat: 'h:mm p',
    // minTime: '10', 
    // maxTime: '6:00pm',
    // defaultTime: '11',
    // startTime: '10:00',
    dynamic: true,
    dropdown: true,
    scrollbar: true

    });
  });
  

// $(document).ready(function() {
//   //jQuery code goes here
// // type check the flight number value ;
// $("#flightnumber").on('input', function() {
//   var input = $(this);
//   var digits = input.val();
//   if(digits){
//       if(digits.length < 5){
//         $("#error-message").removeClass("error_show").addClass("error");
//         console.log("less");}else{
//       $("#error-message").removeClass("error").addClass("error_show");
//       console.log("more");
//   }
//   }
//   console.log(input.val());
// });

// });

$(function() {
  jQuery.validator.setDefaults({
  debug: true,
  success: "valid"
});
// 
//form validator in Jquery 

$( "#travel-data" ).validate({
  rules: {
    carrier : "required",
    arrivaltime: "required",
    departuretime: "required",
    destinationairport:"required",
    departureairport :"required",
    departuredate: "required",
    flightnumber: {
      required: true,
       digits: true,
        min: 1,
        max: 9999,
      }
    },
  messages: {
        flightnumber:{
          min: "The minimum value is 0",
          max: "The maximum value is 9999",
          digits: "The value must be a number",
          required : "Required Field"
        },
        carrier :"We need the carrier Info",
        arrivaltime: "We need arrival time",
        departuretime :"We need departure time",
        destinationairport:"We need destination airport",
        departureairport:"We need departure airport",
        departuredate :"We need departuredate"
        
         }
  
});


});


//date picker 

  $( function() {
    $( 'input.datepicker' ).datepicker({
    	dateFormat : 'DD, dd/mm/yy'
    	
    });
    
  } );


//$(function() {
//
//  $( "form" ).submit(function( event ) {
//	  
//	  event.preventDefault();
//      
//     
//	  
//      $( "span" ).text( "Successfully Submitted !!" ).css('color','#008000').hide();
//
//     var empty = $(this).parent().find("input").filter(function() {
//        return this.value === "";
//      });
//    if(!empty.length) {
//        //At least one input is empty
//        $( "span" ).text( "Successfully Submitted !!" ).css('color','#008000').show();
//    }  
//        //var values = $(this).serialize();
////        var val = $(this).serializeArray();
////        var jsonString = JSON.stringify(val);
//        
//        var val = $('#travel-data').serializeJSON();
//        //var jsonString1 = JSON.stringify(val);
//        
//        //console.log(values);
////        console.log(jsonString);
//        console.log(val); 
//        
////        $.post( '/saveStock', JSON.stringify(val) ); 
//        		
//        $.ajax({
//            type : 'POST',
//            url : '/saveStock',
//            postBody :  JSON.stringify(val),
//            dataType : 'json',
//            success : function(data) {   
//            	console.log("success ajax"+ data);
//            },
//            error: function(jqXHR, textStatus,errorThrown) {
//                var requestResponse = {
//                  httpStatus: jqXHR.status,
//                  error: errorThrown || jqXHR.statusText,
//                };
//
//                console.log(requestResponse);
//            }
//        });
//


  // if ( $( "input:first" ).val() === "correct" ) {
  //   alert("Submitted");
  //   $( "span" ).text( "Successfully Submitted !!" ).css('color','#008000').show();
  // //   return;
  // // }
  // // $( "span" ).text( "Not valid!" ).show().fadeOut( 1000 );
  //  event.preventDefault();
//});
//
//
//});
//  
