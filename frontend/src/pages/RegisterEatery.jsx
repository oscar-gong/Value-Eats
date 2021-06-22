import React from 'react';
import { FloatBox } from '../styles/FloatBox';
import { Subtitle } from '../styles/Subtitle';
import { FileUpload } from '../styles/FileUpload';
import { Label } from '../styles/Label';
import { ImagePreview } from '../styles/ImagePreview';
import { AlignCenter } from '../styles/AlignCenter';
import { Box, TextField, Button } from '@material-ui/core';
import SendIcon from '@material-ui/icons/Send';
import AddAPhotoIcon from '@material-ui/icons/AddAPhoto';

export default function RegisterEatery() {
    const [images, setImages] = React.useState([])

    const handleImages = data => {
        if (data) {
            const fileArray = Array.from(data).map(file => URL.createObjectURL(file))
            setImages(fileArray)
        }
    }

    const previewImages = data => {
        return data.map((photo) => {
            return <ImagePreview src={photo} key={photo}/>
        })
    }

    return (
        <AlignCenter>
            <FloatBox display="flex" flexDirection="column" alignItems="center">
                <Box pt={2}>
                    <Subtitle>Register Eatery</Subtitle>
                </Box>
                <Box width="60%">
                    <TextField id="outlined-basic" label="Email Address" variant="outlined" fullWidth />
                </Box>
                <Box pt={2} width="60%">
                    <TextField id="outlined-basic" label="Password" variant="outlined" fullWidth />
                </Box>
                <Box pt={2} width="60%">
                    <TextField id="outlined-basic" label="Confirm Password" variant="outlined" fullWidth />
                </Box>
                <Box pt={2} width="60%">
                    <TextField id="outlined-basic" label="Eatery Name" variant="outlined" fullWidth />
                </Box>
                <Box pt={2} width="60%">
                    <TextField id="outlined-basic" label="Address" variant="outlined" fullWidth />
                </Box>
                <Box pt={2} width="60%">
                    <TextField id="outlined-basic" label="Cuisines Offered" variant="outlined" fullWidth />
                </Box>
                <Box pt={1}>
                    <Label>
                        <FileUpload type="file" multiple onChange={e => handleImages(e.target.files)}  />
                        {<AddAPhotoIcon/>} Upload Menu Photos
                    </Label>
                    
                </Box>
                <Box flex-wrap= "wrap" flexDirection="row" width="60%">{previewImages(images)}</Box>
                <Box pt={4} pb={4}>
                    <Button variant="contained"
                        color="primary"
                        endIcon={<SendIcon/>}>
                        Register
                    </Button>
                </Box>
            </FloatBox>
        </AlignCenter>
    )
}