const { onRequest } = require("firebase-functions/v2/https");
const admin = require("firebase-admin");
const express = require("express");
const bodyParser = require("body-parser");

// Initialize Firebase Admin
admin.initializeApp(functions.config().firebase);

// Initialize Express app
const app = express();
app.use(bodyParser.json());
app.disable("x-powered-by");

// Callback URL endpoint: /myCallbackUrl
app.post("/myCallbackUrl", (req, res) => {
  const response = {
    ResultCode: 0,
    ResultDesc: "Success",
  };

  // Respond to Safaricom that payload was received successfully
  res.status(200).json(response);

  // Handle payload
  const body = req.body;
  const payload = JSON.stringify(body);

  console.log(payload);

  const id = body.Body?.stkCallback?.CheckoutRequestID || "unknown_checkout_id";

  const payloadSend = {
    data: {
      payload,
    },
    topic: id,
  };

  return admin.messaging().send(payloadSend).catch((error) => {
    console.error("FCM send error:", error);
  });
});

// Export HTTP Cloud Function
exports.api = onRequest({ region: "us-central1" }, app);
