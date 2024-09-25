// Function to save data to Firebase
function saveClientData() {
  const firstName = document.getElementById('firstName').value;
  const lastName = document.getElementById('lastName').value;
  const date = document.getElementById('date').value;
  const age = document.getElementById('age').value;
  const email = document.getElementById('email').value;
  const parentFirstName = document.getElementById('parentFirstName').value;
  const birthDate = document.getElementById('birthDate').value;
  const phone = document.getElementById('phone').value;
  const program = document.getElementById('program').value;
  const area = document.getElementById('area').value;
  const status = document.getElementById('status').value;
  const interest = document.getElementById('interest').value;
  const referral = document.getElementById('referredBy').value;
  const referralName = document.getElementById('referredPersonName').value;

  if (firstName === "" || lastName === "" || age === "" || phone === "" || program === "") {
      alert("Please fill all required fields");
      return;
  }

  document.getElementById('submitButton').style.display = 'none';
  const clientId = firebase.database().ref('client').push().key;  

  const client = {
      id: clientId,
      firstName: firstName,
      lastName: lastName,
      date: date,
      age: age,
      email: email,
      parentFirstName: parentFirstName,
      birthDate: birthDate,
      phone: phone,
      program: program,
      area: area,
      status: status,
      interest: interest,
      referral: referral,
      referralName: referralName
  };

  // Store the JSON object in the specified reference
    firebase.database().ref('client/' + clientId).set(client)
    .then(() => {
        console.log("Data saved successfully!");
        window.location.replace("submitpopup.html");
    })
    .catch((error) => {
    console.error("Error saving data: ", error);
    });
}
