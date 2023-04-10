import type {Meta, StoryObj} from '@storybook/react';

import {KotlinHubToolbar} from "./KotlinHubToolbar";

const meta: Meta<typeof KotlinHubToolbar> = {
    title: "Toolbar",
    component: KotlinHubToolbar,
};

export default meta;
type Story = StoryObj<typeof KotlinHubToolbar>;

export const Default: Story = {
    args: {
        isLoggedIn: false,
    },
}
