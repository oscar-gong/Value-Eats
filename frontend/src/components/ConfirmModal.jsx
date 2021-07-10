import React from "react";
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    Button,
    Box,
} from "@material-ui/core";
import { useHistory } from "react-router-dom";

export default function ConfirmModal({
    open,
    handleClose,
    title,
    message,
    handleConfirm,
    isConfirmVoucher = false,
}) {
    const history = useHistory();
    const handleViewVouchers = () => {
        history.push("/DinerVouchers");
    };
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
                    {!isConfirmVoucher && (
                        <Box>
                            <Button
                                autoFocus
                                onClick={handleClose}
                                color="primary"
                            >
                                Cancel
                            </Button>
                            <Button
                                autoFocus
                                onClick={handleConfirm}
                                color="primary"
                            >
                                Confirm
                            </Button>
                        </Box>
                    )}
                    {isConfirmVoucher && (
                        <Box>
                            <Button
                                autoFocus
                                onClick={handleViewVouchers}
                                color="primary"
                            >
                                View Vouchers
                            </Button>
                            <Button
                                autoFocus
                                onClick={handleConfirm}
                                color="primary"
                            >
                                Ok
                            </Button>
                        </Box>
                    )}
                </DialogActions>
            </Dialog>
        </>
    );
}
