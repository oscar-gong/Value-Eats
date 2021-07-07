import React, {useContext, useState, useEffect } from "react";
import NavBar from "../components/Navbar";
import { MainContent } from "../styles/MainContent";
import { StoreContext } from "../utils/store";
import { Redirect } from "react-router-dom";
import { Box, Button, IconButton } from "@material-ui/core";
import DeleteIcon from '@material-ui/icons/Delete';
import EditIcon from "@material-ui/icons/Edit";

export default function EateryVoucher({discount, isDineIn, vouchersLeft, timeRemaining}) {
  const context = useContext(StoreContext);
  const auth = context.auth[0];
  const isDiner = context.isDiner[0];
  console.log(auth);
  console.log(isDiner);


  if (auth === null) return <Redirect to="/" />;
  if (isDiner === "true") return <Redirect to="/DinerLanding" />;
  console.log(isDiner);


  return (
    <>
      <Box display="flex" justifyContent="space-around" alignItems="center" border="3px solid #4F4846" bgcolor="#E8CEBF" margin="20px">
        <Box display="flex" flexDirection="column">
          <h1>{discount}% off - {isDineIn ? "Dine in" : "Takeaway"}</h1>
        </Box>
        <Box display="flex" flexDirection="column">
          <h3>{vouchersLeft} vouchers remaining...</h3>
          <h3>{timeRemaining} this will be counting down</h3>
        </Box>
        <Box display="flex" flexDirection="column" justifyContent="center">
          <Box display="flex" justifyContent="center">
            <IconButton onClick={() => {}}>
              <EditIcon fontSize="large" />
            </IconButton>
            <IconButton onClick={() => {}}>
              <DeleteIcon fontSize="large" />
            </IconButton>
          </Box>
        </Box>
      </Box>
    </>
  );
}