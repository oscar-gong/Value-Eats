import React, {useContext, useState, useEffect } from "react";
import NavBar from "../components/Navbar";
import { MainContent } from "../styles/MainContent";
import { StoreContext } from "../utils/store";
import { Redirect } from "react-router-dom";
import { Box, Button, IconButton } from "@material-ui/core";
import DeleteIcon from '@material-ui/icons/Delete';
import EditIcon from "@material-ui/icons/Edit";
import ConfirmModal from "./ConfirmModal";

export default function EateryVoucher({voucherId, discount, isDineIn, vouchersLeft, timeRemaining}) {
  const context = useContext(StoreContext);
  const auth = context.auth[0];
  const isDiner = context.isDiner[0];
  const setAlertOptions = context.alert[1];
  console.log(auth);
  console.log(isDiner);
  const [openDeleteModal, setOpenDeleteModal] = useState(false);

  if (auth === null) return <Redirect to="/" />;
  if (isDiner === "true") return <Redirect to="/DinerLanding" />;
  console.log(isDiner);

  const removeVoucher = async (id) => {
    const response = await fetch(
        "http://localhost:8080/eatery/voucher",
        {
            method: "DELETE",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json",
                "Authorization": auth
            },
            body: JSON.stringify({
              "voucherId": id
            })
        }
    );
    const responseData = await response.json();
    if (response.status === 200) {
      setAlertOptions({ showAlert: true, variant: 'success', message: responseData.message });

    } else {
      setAlertOptions({ showAlert: true, variant: 'error', message: responseData.message });
    }
  }

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
              <DeleteIcon fontSize="large" 
                onClick={() => setOpenDeleteModal(true)}
              />
            </IconButton>
          </Box>
        </Box>
      </Box>
      <ConfirmModal open={openDeleteModal}
        handleClose={() => setOpenDeleteModal(false)}
        title={"Delete voucher?"}
        message={`Customers will no longer be able to use the ${vouchersLeft} vouchers remaining, are you sure you want to delete?`}
        handleConfirm={() => removeVoucher(voucherId)}>
      </ConfirmModal>
    </>
  );
}