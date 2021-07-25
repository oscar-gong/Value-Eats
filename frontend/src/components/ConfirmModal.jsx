import React from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  Box,
} from "@material-ui/core";

export default function ConfirmModal ({
  open,
  handleClose,
  title,
  message,
  handleConfirm, // handles text for right button
  confirmText = "Confirm", // handles the function for the right button
  handleDeny = null, // handles text for the left button
  denyText = "Cancel", // handles the function for the left button
}) {
  return (
      <>
        <Dialog
          onClose={handleClose}
          aria-labelledby="customized-dialog-title"
          open={open}
        >
          <DialogTitle id="customized-dialog-title" onClose={handleClose}>
              {title}
          </DialogTitle>
          <DialogContent dividers>{message}</DialogContent>
          <DialogActions>
            <Box>
              <Button
                autoFocus
                onClick={() => {
                  if (handleDeny) {
                    handleDeny();
                  }
                  handleClose();
                }}
                color="primary"
              >
                  {denyText}
              </Button>
              <Button
                autoFocus
                onClick={handleConfirm}
                color="primary"
              >
                {confirmText}
              </Button>
            </Box>
          </DialogActions>
        </Dialog>
    </>
  );
}
