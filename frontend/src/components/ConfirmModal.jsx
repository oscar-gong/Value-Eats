import React from "react";
import {
  Dialog,
  DialogContent,
  DialogActions,
  Box,
  Slide,
} from "@material-ui/core";
import { DialogTitleStyled } from "../styles/DialogTitleStyled";
import { ModalButton } from "../styles/ModalButton";
import { CloseIconStyled } from "../styles/CloseIconStyled";
import CloseIcon from "@material-ui/icons/Close";

const Transition = React.forwardRef(function Transition (props, ref) {
  return <Slide direction="up" ref={ref} {...props} />;
});

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
    <Dialog
      onClose={handleClose}
      aria-labelledby="customized-dialog-title"
      open={open}
      TransitionComponent={Transition}
      keepMounted
    >
      <DialogTitleStyled id="customized-dialog-title" onClose={handleClose}>
        {title}
      </DialogTitleStyled>
      <CloseIconStyled aria-label="close" onClick={handleClose}>
        <CloseIcon />
      </CloseIconStyled>
      <DialogContent dividers>{message}</DialogContent>
      <DialogActions>
        <Box>
          <ModalButton
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
          </ModalButton>
          <ModalButton autoFocus onClick={handleConfirm} color="primary">
            {confirmText}
          </ModalButton>
        </Box>
      </DialogActions>
    </Dialog>
  );
}
