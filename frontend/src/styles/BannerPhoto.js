import styled from 'styled-components';

export const BannerPhoto= styled.img`
    width: 60%;
    max-height: ${props => props.height}px;
    min-height: ${props => props.height}px;
    object-fit: cover;
`