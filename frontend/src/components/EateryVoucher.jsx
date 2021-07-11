import React, {useContext, useState, useEffect } from "react";
import NavBar from "../components/Navbar";
import { MainContent } from "../styles/MainContent";
import { StoreContext } from "../utils/store";
import { Redirect } from "react-router-dom";
import { Box, Button, IconButton } from "@material-ui/core";
import DeleteIcon from '@material-ui/icons/Delete';
import EditIcon from "@material-ui/icons/Edit";
import ConfirmModal from "./ConfirmModal";
import EditCreateVoucher from "../components/EditCreateVoucher";
import Countdown from "react-countdown";

export default function EateryVoucher({eateryId, 
  voucherId, 
  isOneOff, 
  discount, 
  isDineIn, 
  vouchersLeft, 
  date, 
  startTime, 
  endTime, 
  timeRemaining,
  isActive,
  isRedeemable,
  refreshList
}) {
  // console.log(date);

  const convertToDateTime = (date, time) => {
    const datetime = new Date(date);
    const [hours, minutes] = time.split(":");
    console.log(hours);
    console.log(minutes);
    // This accounts for the timezone difference between +10AEST and +0GMT
    datetime.setHours(hours);
    datetime.setMinutes(minutes - datetime.getTimezoneOffset());
    console.log(datetime);
    console.log(datetime.toISOString());
    console.log(datetime.getTimezoneOffset());
    return datetime;
  }
  const startDateTime = convertToDateTime(date, startTime);
  const endDateTime = convertToDateTime(date, endTime);

  // console.log(new Date().getMilliseconds);
  // console.log("HLSKDJHFSDKJFH");
  // const tmpStartCheck = new Date(startDateTime).setMinutes(startDateTime.getMinutes() - startDateTime.getTimezoneOffset());
  // console.log(tmpStartCheck);
  // console.log(Date.now() > (tmpStartCheck.getMilliseconds));

  const context = useContext(StoreContext);
  const auth = context.auth[0];
  const isDiner = context.isDiner[0];
  const setAlertOptions = context.alert[1];
  console.log(auth);
  console.log(isDiner);
  const [openDeleteModal, setOpenDeleteModal] = useState(false);
  const [editCreateModal, setEditCreateModal] = useState(false);

  if (auth === null) return <Redirect to="/" />;
  if (isDiner === "true") return <Redirect to="/DinerLanding" />;
  console.log(isDiner);

  const removeVoucher = async (id) => {
    const response = await fetch(
        "http://localhost:8080/eatery/voucher?id=" + voucherId,
        {
          method: "DELETE",
          headers: {
            "Accept": "application/json",
            "Content-Type": "application/json",
            "Authorization": auth
          }
        }
    );
    const responseData = await response.json();
    if (response.status === 200) {
      setAlertOptions({ showAlert: true, variant: 'success', message: responseData.message });
      refreshList();
    } else {
      setAlertOptions({ showAlert: true, variant: 'error', message: responseData.message });
    }
    setOpenDeleteModal(false);
  }

  return (
    <>
      <Box display="flex" justifyContent="space-around" alignItems="center" border="3px solid #4F4846" bgcolor="#E8CEBF" margin="20px">
        <Box display="flex" flexDirection="column">
          <h1>{discount}% off - {isDineIn ? "Dine in" : "Takeaway"}</h1>
        </Box>
        <Box display="flex" flexDirection="column">
          {
            !isOneOff &&
            <h3 style={{margin: "5px 0px"}}>Weekly deal</h3>
          }
          <h3 style={{margin: "5px 0px"}}>{vouchersLeft} vouchers remaining...</h3>
          {/* <h3 style={{margin: "5px 0px"}}> </h3> */}
          {
            isRedeemable &&
            <Countdown
              onComplete={refreshList}
              date={Date.now() + timeRemaining}
            />
          }
          {
            !isRedeemable && isActive &&
            <h3>Deal starts at {date} {startTime}</h3>
          }
          {
            !isRedeemable && !isActive &&
            <h3>This deal has expired</h3>
          }
        </Box>
        <Box display="flex" flexDirection="column" justifyContent="center">
          <Box display="flex" justifyContent="center">
            <IconButton onClick={() => {}}>
              <EditIcon fontSize="large" 
                onClick={() => setEditCreateModal(true)}
              />
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
      <EditCreateVoucher eateryId={eateryId} voucherId={voucherId}
        open={editCreateModal}
        setOpen={setEditCreateModal}
        isEdit={true}
        initOneOff={isOneOff ? 0 : 1} initDineIn={isDineIn ? "true" : "false"} 
        initDiscount={discount} 
        initQuantity={vouchersLeft}
        initStartTime={startDateTime}
        initEndTime={endDateTime}
        refreshList={refreshList}
      ></EditCreateVoucher>
    </>
  );
}