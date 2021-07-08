import React, { useState, useContext } from 'react';
import { Dialog, DialogTitle, DialogContent, Box, TextField , DialogActions, Button, Tabs, Tab, Radio, RadioGroup, FormControlLabel } from '@material-ui/core';
import { StoreContext } from '../utils/store';
import { validRequired } from '../utils/helpers';

export default function EditCreateVoucher ({ voucherId, open, setOpen, initOneOff=0, initDineIn="true", initDiscount="", initQuantity="", initStartTime="", initEndTime="", isEdit }) {
  const date = new Date();

  const context = useContext(StoreContext);
  const setAlertOptions = context.alert[1];
  const token = context.auth[0];
  const defaultState = (initialValue = "") => {
    return { value: initialValue, valid: true }
  };
  // isOneOff will either be 0 or 1, used for tabs - 1 for is oneoff, 0 for is not oneoff
  const [isOneOff, setIsOneOff] = useState(initOneOff);
  const [isDineIn, setisDineIn] = useState(defaultState(initDineIn));
  const [discount, setDiscount] = useState(defaultState(initDiscount));
  const [quantity, setQuantity] = useState(defaultState(initQuantity));
  const [startDateTime, setStartDateTime] = useState(defaultState(initStartTime === "" ? date.toISOString().split('T')[0] + "T10:30" : initStartTime));
  const [endDateTime, setEndDateTime] = useState(defaultState(initEndTime === "" ? date.toISOString().split('T')[0] + "T10:30" : initEndTime));
  const handleUpdateVoucher = async () => {
    console.log("Make the API call here that will udpate this particular Voucher for a particular restaurant");
  }

  const handleCreateVoucher = async () => {
    // Must first ensure that all the fields are valid
    if (!discount.valid || !quantity.valid || !startDateTime.valid || !endDateTime.valid) {
      return;
    }
    console.log("This will create the voucher");
    // const response = await fetch("http://localhost:8080/diner/createVoucher", 
    //   {
    //     method: "POST",
    //     headers: {
    //       "Accept": "application/json",
    //       "Content-Type": "application/json",
    //       "Authorization": token
    //     },
    //     body: JSON.stringify({
    //       "eateryId": eateryId,
    //       "rating": rating,
    //       "message": reviewText,
    //       "reviewPhotos": images
    //     })
    //   });
    // const responseData = await response.json();
    // if (response.status === 200) {
    //   setAlertOptions({ showAlert: true, variant: 'success', message: responseData.message });
    // } else {
    //   setAlertOptions({ showAlert: true, variant: 'error', message: responseData.message });
    // }
    // setOpen(false);
  }

  // const validDiscount = (discount, setDiscount) => {
  //   if (discount < 0 || discount > 100) {
  //     discount
  //   }
  // }

  const checkValidEndDate = () => {
    const start = new Date(startDateTime.value);
    const end = new Date(endDateTime.value);
    if (start > end) {
      setStartDateTime({...startDateTime, valid: false});
    } else if ((end - start) > 86400000 || (end - start) < 1800000) {
      setEndDateTime({...endDateTime, valid: false});
    }
  }

  return (
    <>
      <Dialog aria-labelledby="customized-dialog-title" open={open}>
        <DialogTitle>
          {isEdit ? "Edit Voucher" : "Create Voucher"}
        </DialogTitle>
        <DialogContent dividers>
          <Box>
            <Tabs value={isOneOff} aria-label="simple tabs example">
              <Tab label="One-off deal" onClick={() => setIsOneOff(0)} />
              <Tab label="Weekly deal" onClick={() => setIsOneOff(1)} />
            </Tabs>
          </Box>
          <Box display="flex" flexDirection="column" alignItems="center">
            <Box pt={2}>
              <RadioGroup row value={isDineIn.value} onChange={(e) => setisDineIn({...isDineIn, value: e.target.value})}>
                <FormControlLabel value="true" control={<Radio />} label="Dine in" />
                <FormControlLabel value="false" control={<Radio />} label="Takeaway" />
              </RadioGroup>
            </Box>
            <Box py={2} width="270px">
              <TextField
                label="Discount (%)"
                type="number"
                onChange={(e) => e.target.value > 0 && e.target.value <= 100 ? 
                  setDiscount({
                    value: e.target.value,
                    valid: true
                  })
                  :
                  setDiscount({
                    value: "",
                    valid: true
                  })
                }
                allowNegative={false}
                error={!discount.valid}
                helperText={
                  discount.valid ? "" : "Please enter a discount percentage"
                }
                onBlur={() => {
                  validRequired(discount, setDiscount);
                }}
                value={discount.value}
                variant="outlined"
                fullWidth
              />
            </Box>
            <Box py={2} width="270px">
              <TextField
                label="Quantity"
                type="number"
                onChange={(e) => e.target.value > 0 ? 
                  setQuantity({
                    value: e.target.value,
                    valid: true
                  })
                  :
                  setQuantity({
                    value: "",
                    valid: true
                  })
                }
                allowNegative={false}
                error={!quantity.valid}
                helperText={
                  quantity.valid ? "" : "Please enter the number of vouchers you would like to offer"
                }
                onBlur={() => {
                  validRequired(quantity, setQuantity);
                }}
                value={quantity.value}
                variant="outlined"
                fullWidth
            />
            </Box>
            <Box py={2}>
              <TextField
                label="Start at:"
                type="datetime-local"
                onChange={(e) => {
                  setStartDateTime({
                    value: e.target.value,
                    valid: true
                  });
                  setEndDateTime({...endDateTime, valid: true});
                }
                }
                error={!startDateTime.valid}
                helperText={
                  startDateTime.valid ? "" : "Start time must be before the end time"
                }
                onBlur={() => {
                  checkValidEndDate();
                }}
                value={startDateTime.value}
                variant="outlined"
                fullWidth
              />
            </Box>
            <Box py={2}>
              <TextField
                label="Start at:"
                type="datetime-local"
                onChange={(e) => {
                  setEndDateTime({
                    value: e.target.value,
                    valid: true
                  });
                  setStartDateTime({...startDateTime, valid: true});
                }
                }
                error={!endDateTime.valid}
                helperText={
                  endDateTime.valid ? "" : "Deal must last at least 30 minutes and at most 1 day"
                }
                onBlur={() => {
                  checkValidEndDate();
                }}
                value={endDateTime.value}
                variant="outlined"
                fullWidth
              />
            </Box>
          </Box>
        </DialogContent>
        <DialogActions>
          {/* Set the things below back to their default states here */}
          <Button autoFocus onClick={() => {setOpen(false)}} color="primary">
            Cancel
          </Button>
          <Button autoFocus onClick={isEdit ? handleUpdateVoucher : handleCreateVoucher} color="primary">
            {isEdit ? "Save changes" : "Create voucher"}
          </Button>
        </DialogActions>
      </Dialog>
    </>
  )
}